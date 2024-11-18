package veniamin.tasksapp.backend.exception;

import veniamin.tasksapp.backend.exception.errors.NotFoundError;
import lombok.Getter;

@Getter
public class NotFoundException extends BusinessException {

    private String errorName;

    public NotFoundException(NotFoundError notFoundError) {
        super(notFoundError.getMessage());
        errorName = notFoundError.name();
    }

    public NotFoundException(String message, String errorName) {
        super(message);
        this.errorName = errorName;
    }
}
