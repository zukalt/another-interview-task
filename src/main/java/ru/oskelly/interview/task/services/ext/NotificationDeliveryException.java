package ru.oskelly.interview.task.services.ext;

public class NotificationDeliveryException extends CommentHandlerException {
    final long notificationId;

    public NotificationDeliveryException(long notificationId, long commentId, String message, Throwable cause){
        super(commentId, message, cause);
        this.notificationId = notificationId;
    }

    public long getNotificationId() {
        return notificationId;
    }
}
