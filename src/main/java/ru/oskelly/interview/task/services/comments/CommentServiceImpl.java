package ru.oskelly.interview.task.services.comments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.oskelly.interview.task.model.Comment;
import ru.oskelly.interview.task.model.Notification;
import ru.oskelly.interview.task.services.ext.CommentHandlerException;
import ru.oskelly.interview.task.services.ext.NewCommentHandler;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

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
                .thenApply(c -> {
                    deliverNotifications(c);
                    return c;
                })
                .exceptionally(t -> {
                    t = (t instanceof CompletionException) ? t.getCause() : t;

                    if (t instanceof CommentHandlerException) {
                        long commentId = ((CommentHandlerException) t).getCommentId();
                        logger.warn("Removing comment #{} due to processing error",  commentId);
                        commentsRepository.removeComment(commentId);
                    } else {
                        logger.error("Unexpected error during comment processing:",  t);
                    }
                    throw new CommentHandlerException(-1, t.getMessage(), t);
                });
    }

    @Override
    public CompletableFuture<List<Comment>> listComments(long before, int pageSize) {
        return completedFuture(commentsRepository.listComments(before, pageSize));
    }

    @Override
    public CompletableFuture<List<Notification>> listNotifications(long before, int pageSize) {
        return completedFuture(notificationRepository.listNotifications(before, pageSize));
    }

    private CompletableFuture<Comment> callNewCommentHandler(Comment c) {
        return commentHandler.doOnNewComment(c)
                .thenApply(commentsRepository::getCommentById);
    }

    private void deliverNotifications(Comment comment) {
        final Notification notification = notificationRepository.save(new Notification(comment));
        commentHandler.doOnNotification(notification)
                .thenAccept(n -> notificationRepository.markDelivered(n, new Date()))
                .whenComplete((r, t) -> {
                    if (t != null) {
                        long commentId = -1;
                        if (t.getCause() instanceof CommentHandlerException) {
                            commentId = ((CommentHandlerException)t.getCause()).getCommentId();
                        }
                        logger.warn("Notification delivery failed for comment #{}", commentId);
                    }
                })
        ;
    }
}
