package ru.oskelly.interview.task.services.ext;

public class CommentHandlerException extends RuntimeException {
    final long commentId;
    public CommentHandlerException(long commentId, String message, Throwable cause){
        super(message, cause);
        this.commentId = commentId;
    }

    public long getCommentId() {
        return commentId;
    }
}
