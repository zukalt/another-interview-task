package ru.oskelly.interview.task.services.ext;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.oskelly.interview.task.model.Comment;
import ru.oskelly.interview.task.model.Notification;

import java.util.concurrent.CompletableFuture;

@Service
public class NewCommentHandlerProxy implements NewCommentHandler {

    private final AmqpTemplate template;
    private final String commentsQueue;
    private final String notificationsQueue;

    public NewCommentHandlerProxy(AmqpTemplate template,
                                  @Value("${app.queue.comments}") String commentsQueue,
                                  @Value("${app.queue.notifications}") String notificationsQueue) {
        this.template = template;
        this.commentsQueue = commentsQueue;
        this.notificationsQueue = notificationsQueue;
    }

    @Override
    public CompletableFuture<Long> doOnNewComment(Comment comment) {
        final Long id = comment.getId();
        return CompletableFuture
                .supplyAsync(() -> (String) template.convertSendAndReceive(commentsQueue, id))
                .handle((response, t) -> {
                    if (t != null || !"ok".equals(response)) {
                        throw new CommentHandlerException(id, "Comments handling failed", t);
                    }
                    return id;
                });
    }

    @Override
    public CompletableFuture<Long> doOnNotification(Notification notification) {
        final long id = notification.getId();
        final long commentId = notification.getComment().getId();
        return CompletableFuture
                .supplyAsync(() -> (String) template.convertSendAndReceive(notificationsQueue, id))
                .handle((response, t) -> {
                    if (t != null || !"ok".equals(response)) {
                        throw new NotificationDeliveryException(id, commentId, "Delivery failed", t);
                    }
                    return id;
                });
    }

}
