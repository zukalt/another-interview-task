package ru.oskelly.interview.task.services.comments;

import ru.oskelly.interview.task.model.Comment;
import ru.oskelly.interview.task.model.Notification;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CommentsService {

    CompletableFuture<Comment> addComment(String comment);

    CompletableFuture<List<Comment>> listComments(long from, int pageSize);

    CompletableFuture<List<Notification>> listNotifications(long before, int pageSize);
}
