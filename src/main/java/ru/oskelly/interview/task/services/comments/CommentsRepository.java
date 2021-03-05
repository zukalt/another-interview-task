package ru.oskelly.interview.task.services.comments;

import ru.oskelly.interview.task.model.Comment;

import java.util.List;

public interface CommentsRepository {

    Comment addComment(Comment comment);
    void removeComment(Comment c);
    Comment getCommentById(long id);
    List<Comment> listComments(int page, int pageSize);

}
