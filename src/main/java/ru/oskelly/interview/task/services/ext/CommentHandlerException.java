package ru.oskelly.interview.task.services.ext;

public class CommentHandlerException extends RuntimeException {

    public CommentHandlerException(String message, Throwable cause){
        super(message, cause);
    }
}
