package ru.oskelly.interview.task.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CreateCommentDto {
    private final String comment;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CreateCommentDto(@JsonProperty("comment") String comment) {
        this.comment = comment;
    }
}
