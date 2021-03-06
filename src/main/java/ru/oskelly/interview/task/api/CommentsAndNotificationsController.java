package ru.oskelly.interview.task.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.oskelly.interview.task.api.dto.CommentDto;
import ru.oskelly.interview.task.api.dto.CreateCommentDto;
import ru.oskelly.interview.task.api.dto.NotificationDto;
import ru.oskelly.interview.task.services.comments.CommentsService;
import ru.oskelly.interview.task.services.ext.CommentHandlerException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Api(tags = "Comments & Notifications")
@ApiResponses({
        @ApiResponse(code = 503, message = "Failed to add comment"),
        @ApiResponse(code = 500, message = "Internal Server Error")
})
@Controller()
@RequestMapping("api")
@ResponseBody
public class CommentsAndNotificationsController {

    private final CommentsService commentsService;

    public CommentsAndNotificationsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @PostMapping("comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<CommentDto> postComment(@RequestBody CreateCommentDto newComment) {
        return commentsService.addComment(newComment.getComment())
                .thenApply(CommentDto::fromEntity);
    }

    @GetMapping("comments")
    public CompletableFuture<List<CommentDto>> searchComments(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {
        return commentsService.listComments(page, pageSize)
                .thenApply(CommentDto::fromEntities);
    }


    @GetMapping("notifications")
    public CompletableFuture<List<NotificationDto>> searchNotifications(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {
        return commentsService.listNotifications(page, pageSize)
                .thenApply(NotificationDto::fromEntities);
    }

    @ExceptionHandler
    private ResponseEntity<String> handleExceptions(CommentHandlerException e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
    }

    @ExceptionHandler
    private ResponseEntity<String> handleExceptions(Throwable e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }


}
