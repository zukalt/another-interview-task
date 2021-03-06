package ru.oskelly.interview.task.services.ext;

import org.springframework.stereotype.Service;
import ru.oskelly.interview.task.model.Comment;
import ru.oskelly.interview.task.model.Notification;

import java.util.concurrent.CompletableFuture;

@Service
public class NewCommentHandlerProxy implements NewCommentHandler {

    @Override
    public CompletableFuture<Long> doOnNewComment(Comment comment) {
        final Long id = comment.getId();
        return CompletableFuture.supplyAsync(() -> {
            BusinessLogic.doSomeWorkOnCommentCreation();
            return id;
        }).exceptionally(t -> {throw new CommentHandlerException("Comments handling failed", t);});
    }

    @Override
    public CompletableFuture<Long> doOnNotification(Notification notification) {
        final Long id = notification.getId();
        return CompletableFuture.supplyAsync(() -> {
            BusinessLogic.doSomeWorkOnNotification();
            return id;
        }).exceptionally(t -> {throw new CommentHandlerException("Notification delivery failed", t);});
    }

    public static class BusinessLogic {
        public static void sleepAndRandomThrowRuntimeException(int seconds, int exceptionProbabilityProc) {
            try {
                Thread.sleep((long) (seconds * 1000 * Math.random()));
            } catch (InterruptedException ignored) {
            }
            int randomProc = (int) (100 * Math.random());
            if (exceptionProbabilityProc > randomProc) throw new RuntimeException();
        }

        public static void doSomeWorkOnNotification() {
            sleepAndRandomThrowRuntimeException(2, 10);
        }

        public static void doSomeWorkOnCommentCreation() {
            sleepAndRandomThrowRuntimeException(1, 30);
        }
    }
}
