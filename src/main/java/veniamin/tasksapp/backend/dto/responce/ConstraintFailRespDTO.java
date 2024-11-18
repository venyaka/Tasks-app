package veniamin.tasksapp.backend.dto.responce;

import lombok.Data;

@Data
public class ConstraintFailRespDTO {

    private String fieldName;

    private String message;

    private Object rejectedValue;

    private String code;
}
