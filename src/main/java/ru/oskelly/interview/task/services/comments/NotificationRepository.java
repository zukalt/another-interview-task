package ru.oskelly.interview.task.services.comments;

import ru.oskelly.interview.task.model.Comment;
import ru.oskelly.interview.task.model.Notification;

import java.util.List;

public interface NotificationRepository {
    Notification save(Notification notification);
    void markDelivered(Long notificationId);
    List<Notification> listNotifications(int page, int pageSize);
}
