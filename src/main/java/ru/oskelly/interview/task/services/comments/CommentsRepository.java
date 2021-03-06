package ru.oskelly.interview.task.services.comments;

import ru.oskelly.interview.task.model.Comment;

import java.util.List;

public interface CommentsRepository {

    Comment addComment(Comment comment);
    void removeComment(long id);
    Comment getCommentById(long id);
    List<Comment> listComments(long before, int pageSize);

}
