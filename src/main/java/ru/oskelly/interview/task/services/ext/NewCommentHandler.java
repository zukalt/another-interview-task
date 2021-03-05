package ru.oskelly.interview.task.services.ext;

import ru.oskelly.interview.task.model.Comment;
import ru.oskelly.interview.task.model.Notification;

import java.util.concurrent.CompletableFuture;

public interface NewCommentHandler {

    CompletableFuture<Long>  doOnNewComment(Comment comment);

    CompletableFuture<Long> doOnNotification(Notification notification);
}
