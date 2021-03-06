package ru.oskelly.interview.task.repositories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.oskelly.interview.task.model.Comment;
import ru.oskelly.interview.task.services.comments.CommentsRepository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CommentsRepositoryJpa extends JpaRepository<Comment, Long>, CommentsRepository {

    default Comment addComment(Comment comment) {
        return save(comment);
    }

    default Comment getCommentById(long id) {
        return findById(id).orElseThrow();
    }

    List<Comment> findAllByIdLessThanOrderByIdDesc(long id, Pageable pageable);

    default List<Comment> listComments(long before, int pageSize) {
        return findAllByIdLessThanOrderByIdDesc(before, PageRequest.of(0, pageSize));
    }

    @Transactional()
    @Modifying
    @Query("delete from Comment c where c.id = ?1 ")
    void removeComment(long id);
}
