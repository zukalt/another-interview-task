package ru.oskelly.interview.task.repositories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.oskelly.interview.task.model.Comment;
import ru.oskelly.interview.task.services.comments.CommentsRepository;

import java.util.List;

@Repository
public interface CommentsRepositoryJpa extends JpaRepository<Comment, Long>, CommentsRepository {

    default Comment addComment(Comment comment) {
        return save(comment);
    }

    default Comment getCommentById(long id) {
        return findById(id).orElseThrow();
    }

    default List<Comment> listComments(int page, int pageSize) {
        return findAll(PageRequest.of(page, pageSize, Sort.by("id"))).getContent();
    }

    default void removeComment(Comment c){
        delete(c);
    }
}
