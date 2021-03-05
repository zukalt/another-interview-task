package ru.oskelly.interview.task.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.oskelly.interview.task.model.Notification;
import ru.oskelly.interview.task.services.comments.NotificationRepository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface NotificationRepositoryJpa
        extends org.springframework.data.repository.Repository<Notification, Long>
        , NotificationRepository {


    Page<Notification> findAll(Pageable pageable);

    default List<Notification> listNotifications(int page, int pageSize) {
        return findAll(PageRequest.of(page, pageSize, Sort.by("id"))).getContent();
    }

    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.delivered = true, n.deliveryTime = now() WHERE n.id = ?1")
    void markDelivered(Long notificationId) ;
}
