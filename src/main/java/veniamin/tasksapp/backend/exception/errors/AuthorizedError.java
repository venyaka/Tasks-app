package veniamin.tasksapp.backend.exception.errors;

public enum AuthorizedError {

    NOT_AUTHENTICATED("Пользователь не авторизован"),

    TOKEN_WAS_EXPIRED("Срок действия токена истек"),

    BAD_CREDENTIALS("Неверные данные для авторизации"),

    USER_NOT_VERIFY("Пользователь не верифицирован"),

    USER_WITH_THIS_EMAIL_NOT_FOUND("Пользователь с данным email не найден в системе"),

    TASK_NOT_FOUND("Задача не найден в системе"),

    NOT_CORRECT_PASSWORD("Неверный пароль"),

    USER_IS_DELETED("Пользователь удален"),

    USER_IS_NOT_ACTIVE("Пользователь не активен"),

    NOT_CORRECT_TOKEN("Неверный токен доступа");


    private String message;

    AuthorizedError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
