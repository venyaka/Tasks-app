package veniamin.tasksapp.backend.dto.request.task;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskCommentChangeReqDTO {

    @NotBlank
    private Long id;

    private String comment;
}
