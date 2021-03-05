package ru.oskelly.interview.task.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor()
public class Comment {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String  comment;

    @Column(name = "time", nullable = false)
    private Date createdAt;

    public Comment(String comment) {
        this.comment = comment;
        this.createdAt = new Date();
    }
}
