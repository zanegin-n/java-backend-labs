package ru.zanegin.exception;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message); // конструктор только с сообщением об ошибке
    }
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);  // конструктор с причиной
    }
}