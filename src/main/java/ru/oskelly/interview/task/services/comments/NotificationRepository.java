package ru.oskelly.interview.task.services.comments;

import ru.oskelly.interview.task.model.Notification;

import java.util.Date;
import java.util.List;

public interface NotificationRepository {
    Notification save(Notification notification);
    void markDelivered(Long notificationId, Date deliveryTime);
    List<Notification> listNotifications(long before, int pageSize);
}
