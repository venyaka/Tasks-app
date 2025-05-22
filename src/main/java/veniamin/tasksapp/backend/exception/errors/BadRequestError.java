package veniamin.tasksapp.backend.exception.errors;

public enum BadRequestError {

    TASK_NOT_BELONG_TO_THIS_USER("Курс не принадлежит данному пользователю"),
    NOT_CORRECT_PASSWORD("Неверный пароль"),
    NOT_CORRECT_REFRESH_TOKEN("Неверный рефреш токен"),
    USER_ALREADY_VERIFICATED("Пользователь уже был верифицирован"),
    USER_NOT_VERIFICATED("Пользователь не верифицирован"),
    NOT_CORRECT_VERIFICATION_CODE("Код верификации не корректен или не был запрошен"),
    USER_ALREADY_EXISTS("Пользователь с такой почтой уже существует");

    private String message;

    BadRequestError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
