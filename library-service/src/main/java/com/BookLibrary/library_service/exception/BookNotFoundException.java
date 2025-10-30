package com.BookLibrary.library_service.exception;

public class BookNotFoundException extends RuntimeException{
    private ExceptionMessage exceptionMessage;
    public BookNotFoundException(String message) {
        super(message);
    }

    public BookNotFoundException(ExceptionMessage message) {
        this.exceptionMessage = message;
    }

    public BookNotFoundException(String message, ExceptionMessage exceptionMessage) {
        super(message);
        this.exceptionMessage = exceptionMessage;
    }

    public ExceptionMessage getExceptionMessage() {
        return exceptionMessage;
    }

}
