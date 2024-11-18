package veniamin.tasksapp.backend.dto.responce;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BusinessExceptionRespDTO {

    private LocalDateTime timestamp;

    private Long status;

    private String error;

    private String message;

    private String path;

    private String debugInfo;
}
