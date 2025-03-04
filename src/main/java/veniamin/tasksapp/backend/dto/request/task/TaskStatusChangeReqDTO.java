package veniamin.tasksapp.backend.dto.request.task;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskStatusChangeReqDTO {

    @NotBlank
    private Long id;

    @NotBlank
    private Boolean isComplete;
}
