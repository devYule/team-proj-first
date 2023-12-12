package team6.project.common.exception;

public class TodoIsFullException extends RuntimeException{
    public TodoIsFullException(String message) {
        super(message);
    }
}
