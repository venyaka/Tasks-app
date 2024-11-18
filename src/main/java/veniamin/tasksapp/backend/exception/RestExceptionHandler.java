package veniamin.tasksapp.backend.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import veniamin.tasksapp.backend.dto.responce.BusinessExceptionRespDTO;
import veniamin.tasksapp.backend.dto.responce.ConstraintFailRespDTO;
import veniamin.tasksapp.backend.dto.responce.ValidationExceptionRespDTO;
import veniamin.tasksapp.backend.utils.LogsUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final ObjectMapper objectMapper;
    private final LogsUtils logsUtils;

    private static final Logger loggerFileError = LoggerFactory.getLogger("fileErrorLogger");

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public BusinessExceptionRespDTO handleExceptions(
            NotFoundException ex, HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
        Long httpStatusCode = 404L;
        BusinessExceptionRespDTO businessExceptionRespDTO = formBusinessExceptionDTO(httpStatusCode, ex.getErrorName(), ex.getMessage(), request.getRequestURI());

        logsUtils.log(loggerFileError, ex.getMessage() + ". " + request.getMethod().toString() + request.getRequestURI().toString(), ex);
        return businessExceptionRespDTO;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public BusinessExceptionRespDTO handleExceptions(Throwable ex, HttpServletRequest request, HttpServletResponse httpServletResponse) {
        Long httpStatusCode = 500L;
        String stackTrace = Arrays.stream(ex.getStackTrace())
                .map(stackTraceElement -> stackTraceElement.toString())
                .reduce("", (frstStr, scndStr) -> frstStr + "\n " + scndStr);
        stackTrace = stackTrace.length() > 200 ? stackTrace.substring(0, 200) : stackTrace;

        BusinessExceptionRespDTO businessExceptionRespDTO = formBusinessExceptionDTO(httpStatusCode, "INTERNAL_SERVER_ERROR",
                ex.getMessage(), request.getRequestURI());
        businessExceptionRespDTO.setDebugInfo(stackTrace);

        logsUtils.log(loggerFileError, ex.getMessage() + ". " + request.getMethod().toString() + request.getRequestURI().toString(), ex);
        return businessExceptionRespDTO;
    }

    @ExceptionHandler(PropertyReferenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BusinessExceptionRespDTO handleExceptions(
            PropertyReferenceException ex, HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
        Long httpStatusCode = 400L;
        BusinessExceptionRespDTO businessExceptionRespDTO = formBusinessExceptionDTO(httpStatusCode, "BAD_REQUEST", ex.getMessage(), request.getRequestURI());

        logsUtils.log(loggerFileError, ex.getMessage() + ". " + request.getMethod().toString() + request.getRequestURI().toString(), ex);
        return businessExceptionRespDTO;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BusinessExceptionRespDTO handleExceptions(
            ConstraintViolationException ex, HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
        Long httpStatusCode = 400L;
        BusinessExceptionRespDTO businessExceptionRespDTO = formBusinessExceptionDTO(httpStatusCode, "BAD_REQUEST", ex.getMessage(), request.getRequestURI());
        businessExceptionRespDTO.setDebugInfo(ex.getConstraintViolations().toString());

        logsUtils.log(loggerFileError, ex.getMessage() + ". " + request.getMethod().toString() + request.getRequestURI().toString(), ex);
        return businessExceptionRespDTO;
    }


//    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        Long httpStatusCode = 400L;
        String debugInfo = ex.getMessage();
        ValidationExceptionRespDTO validationExceptionRespDTO = formValidationExceptionRespDTO(httpStatusCode, "BAD_REQUEST", validationErrorsFormat(ex.getFieldErrors()),
                ((ServletWebRequest) request).getRequest().getRequestURI().toString(), ex.getFieldErrors());
        validationExceptionRespDTO.setDebugInfo(debugInfo);

        logsUtils.log(loggerFileError, ex.getMessage() + ". " + ((ServletWebRequest) request).getRequest().getMethod().toString() + ((ServletWebRequest) request).getRequest().getRequestURI().toString(), ex);
        return new ResponseEntity<>(validationExceptionRespDTO, status);
    }


    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public BusinessExceptionRespDTO handleExceptions(
            AccessDeniedException ex, HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {

        Long httpStatusCode = 403L;
        String errorName = "FORBIDDEN";

        logsUtils.log(loggerFileError, ex.getMessage() + ". " + request.getMethod().toString() + request.getRequestURI().toString(), ex);
        return formBusinessExceptionDTO(httpStatusCode, errorName, ex.getMessage(), request.getRequestURI());
    }


    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public BusinessExceptionRespDTO handleExceptions(
            AuthenticationException ex, HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
        Long httpStatusCode = 401L;
        String errorName = "BAD CREDENTIALS";

        logsUtils.log(loggerFileError, ex.getMessage() + ". " + request.getMethod().toString() + request.getRequestURI().toString(), ex);
        return formBusinessExceptionDTO(httpStatusCode, errorName, ex.getMessage(), request.getRequestURI());
    }


    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BusinessExceptionRespDTO handleExceptions(
            BadRequestException ex, HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
        Long httpStatusCode = 400L;

        logsUtils.log(loggerFileError, ex.getMessage() + ". " + request.getMethod().toString() + request.getRequestURI().toString(), ex);
        return formBusinessExceptionDTO(httpStatusCode, ex.getErrorName(), ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(AuthorizeException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public BusinessExceptionRespDTO handleExceptions(
            AuthorizeException ex, HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
        Long httpStatusCode = 401L;

        logsUtils.log(loggerFileError, ex.getMessage() + ". " + request.getMethod().toString() + request.getRequestURI().toString(), ex);
        return formBusinessExceptionDTO(httpStatusCode, ex.getErrorName(), ex.getMessage(), request.getRequestURI());
    }


    private BusinessExceptionRespDTO formBusinessExceptionDTO(Long status, String errorName, String message, String path) {
        BusinessExceptionRespDTO businessExceptionRespDTO = new BusinessExceptionRespDTO();
        businessExceptionRespDTO.setError(errorName);
        businessExceptionRespDTO.setMessage(message);
        businessExceptionRespDTO.setPath(path);
        businessExceptionRespDTO.setTimestamp(LocalDateTime.now());
        businessExceptionRespDTO.setStatus(status);
        return businessExceptionRespDTO;
    }

    private String validationErrorsFormat(List<FieldError> fieldErrorList) {
        StringBuilder sb = new StringBuilder();
        fieldErrorList.stream().forEach(error -> {
            String fieldName = error.getField();
            String defaultMessage = error.getDefaultMessage();
            sb.append(fieldName + " " + defaultMessage + " ");
        });
        return sb.toString();
    }

    private ValidationExceptionRespDTO formValidationExceptionRespDTO(Long status, String errorName, String message, String path, List<FieldError> fieldErrorList) {
        ValidationExceptionRespDTO validationExceptionRespDTO = new ValidationExceptionRespDTO();
        validationExceptionRespDTO.setError(errorName);
        validationExceptionRespDTO.setMessage(message);
        validationExceptionRespDTO.setPath(path);
        validationExceptionRespDTO.setTimestamp(LocalDateTime.now());
        validationExceptionRespDTO.setStatus(status);
        List<ConstraintFailRespDTO> constraintFailRespDTOList = fieldErrorList.stream().map(fieldError -> {
            ConstraintFailRespDTO constraintFailRespDTO = new ConstraintFailRespDTO();
            constraintFailRespDTO.setFieldName(fieldError.getField());
            constraintFailRespDTO.setRejectedValue(fieldError.getRejectedValue());
            constraintFailRespDTO.setMessage(fieldError.getDefaultMessage());
            constraintFailRespDTO.setCode(fieldError.getCode());
            return constraintFailRespDTO;
        }).collect(Collectors.toList());
        validationExceptionRespDTO.setConstraintFailRespDTOList(constraintFailRespDTOList);
        return validationExceptionRespDTO;
    }
}
