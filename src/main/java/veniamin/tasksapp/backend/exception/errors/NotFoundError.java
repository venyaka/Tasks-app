package veniamin.tasksapp.backend.exception.errors;

public enum NotFoundError {
    USER_NOT_FOUND("Пользователь не был найден"),
    TASK_NOT_FOUND("Задача не была найдена");

    private String message;

    NotFoundError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
