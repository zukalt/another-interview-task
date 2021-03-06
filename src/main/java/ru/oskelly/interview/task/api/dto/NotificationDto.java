package ru.oskelly.interview.task.api.dto;

import lombok.Getter;
import ru.oskelly.interview.task.model.Notification;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class NotificationDto {

    private final long id;
    private final long commentId;
    private final boolean delivered;
    private final Date deliveryTime;

    public NotificationDto(long id, long commentId, boolean delivered, Date deliveryTime) {
        this.id = id;
        this.commentId = commentId;
        this.delivered = delivered;
        this.deliveryTime = deliveryTime;
    }

    public static List<NotificationDto> fromEntities(List<Notification> notifications) {
        return notifications.stream().map(n -> new NotificationDto(n.getId(), n.getComment().getId(), n.isDelivered(), n.getDeliveryTime())).collect(Collectors.toList());
    }
}
