package ru.oskelly.interview.task.api;

import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.oskelly.interview.task.api.dto.CommentDto;
import ru.oskelly.interview.task.api.dto.CreateCommentDto;
import ru.oskelly.interview.task.api.dto.NotificationDto;
import ru.oskelly.interview.task.model.Comment;
import ru.oskelly.interview.task.model.Notification;
import ru.oskelly.interview.task.services.comments.CommentsService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Api(tags = "Comments & Notifications")
@Controller()
@RequestMapping("api")
public class CommentsAndNotificationsController {

    private final CommentsService commentsService;

    public CommentsAndNotificationsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @PostMapping("comments")
    public CompletableFuture<CommentDto> postComment(@RequestBody CreateCommentDto newComment) {
        CompletableFuture<Comment> future = commentsService.addComment(newComment.getComment());
        return future.thenApply(CommentDto::fromEntity);
    }

    @GetMapping("comments")
    public CompletableFuture<List<CommentDto>> searchComments(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {
        CompletableFuture<List<Comment>> future = commentsService.listComments(page, pageSize);
        return future.thenApply(CommentDto::fromEntities);
    }


    @GetMapping("notifications")
    public CompletableFuture<List<NotificationDto>> searchNotifications(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {
        CompletableFuture<List<Notification>> future = commentsService.listNotifications(page, pageSize);
        return future.thenApply(NotificationDto::fromEntities);
    }

    @ExceptionHandler
    private ResponseEntity<String> handleExceptions(RuntimeException re) {
        return ResponseEntity.status(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED).body("Oops... :/");
    }


}
