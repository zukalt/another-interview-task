package ru.oskelly.interview.task.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    private Comment comment;

    private boolean delivered = false;

    @Column(name = "time")
    private Date deliveryTime;

    public Notification(Comment comment){
        this.comment = comment;
    }
}
