package ru.oskelly.interview.task.services.ext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("with-rpc-handler")
public class RemoteCommentsRPCHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RabbitListener(queues = "${app.queue.comments}", concurrency = "30")
    public String onNewComment(long commentId) {
        logger.info("Handling comment #{}", commentId);
        try {
            BusinessLogic.doSomeWorkOnCommentCreation();
            return "ok";
        }
        catch (RuntimeException ignore) {}
        return "failed";
    }

    @RabbitListener(queues = "${app.queue.notifications}", concurrency = "30")
    public String notifyFor(long notificationId) {
        logger.info("Delivering notification #{}", notificationId);
        try {
            BusinessLogic.doSomeWorkOnNotification();
            return "ok";
        }
        catch (RuntimeException ignore) {}
        return "failed";
    }
}
