package ru.oskelly.interview.task.services.comments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.oskelly.interview.task.model.Comment;
import ru.oskelly.interview.task.model.Notification;
import ru.oskelly.interview.task.services.ext.NewCommentHandler;

import java.util.List;
import java.util.concurrent.CompletableFuture;


import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;

@Service
public class CommentServiceImpl implements CommentsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final CommentsRepository commentsRepository;
    private final NotificationRepository notificationRepository;
    private final NewCommentHandler commentHandler;

    public CommentServiceImpl(CommentsRepository commentsRepository, NotificationRepository notificationRepository, NewCommentHandler commentHandler) {
        this.commentsRepository = commentsRepository;
        this.notificationRepository = notificationRepository;
        this.commentHandler = commentHandler;
    }

    @Override
    public CompletableFuture<Comment> addComment(String commentText) {
        return supplyAsync(() -> commentsRepository.addComment(new Comment(commentText)))
                .thenCompose(this::callNewCommentHandler)
                .whenComplete((comment, t) -> {
                    if (t != null) {
                        logger.error("Comment handling failed: {}", t.getMessage());
                        commentsRepository.removeComment(comment);
                        throw new RuntimeException(t);
                    } else {
                        deliverNotifications(comment);
                    }
                });
    }

    @Override
    public CompletableFuture<List<Comment>> listComments(int page, int pageSize) {
        return completedFuture(commentsRepository.listComments(page, pageSize));
    }

    @Override
    public CompletableFuture<List<Notification>> listNotifications(int page, int pageSize) {
        return completedFuture(notificationRepository.listNotifications(page, pageSize));
    }

    private CompletableFuture<Comment> callNewCommentHandler(Comment c) {
        return commentHandler.doOnNewComment(c)
                .thenApply(commentsRepository::getCommentById);
    }

    private void deliverNotifications(Comment comment) {
        final Notification notification = notificationRepository.save(new Notification(comment));
        commentHandler.doOnNotification(notification)
                .thenAccept(notificationRepository::markDelivered)
                .whenComplete((r, t) -> {
                    logger.warn("Notification delivery failed: {}", t.getMessage());
                })
        ;
    }
}
