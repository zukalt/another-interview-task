package ru.oskelly.interview.task.api.dto;

import lombok.*;
import ru.oskelly.interview.task.model.Comment;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentDto {
    private final long id;
    private final String comment;
    private final Date createdAt;

    public CommentDto(long id, String comment, Date createdAt) {
        this.id = id;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public static CommentDto fromEntity(Comment entity) {
        return new CommentDto(entity.getId(), entity.getComment(), entity.getCreatedAt());
    }

    public static List<CommentDto> fromEntities(List<Comment> comments) {
        return comments.stream().map(CommentDto::fromEntity).collect(Collectors.toList());
    }
}
