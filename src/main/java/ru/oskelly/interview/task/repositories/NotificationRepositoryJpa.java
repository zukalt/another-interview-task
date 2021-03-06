package ru.oskelly.interview.task.repositories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.oskelly.interview.task.model.Notification;
import ru.oskelly.interview.task.services.comments.NotificationRepository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface NotificationRepositoryJpa
        extends org.springframework.data.repository.Repository<Notification, Long>
        , NotificationRepository {


    List<Notification> findAllByIdLessThanOrderByIdDesc(long id, Pageable pageable);

    default List<Notification> listNotifications(long before, int pageSize) {
        return findAllByIdLessThanOrderByIdDesc(before, PageRequest.of(0, pageSize));
    }

    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.delivered = true, n.deliveryTime = ?2 WHERE n.id = ?1")
    void markDelivered(Long notificationId, Date deliveryTime) ;
}
