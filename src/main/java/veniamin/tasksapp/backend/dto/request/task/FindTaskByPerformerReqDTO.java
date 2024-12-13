package veniamin.tasksapp.backend.dto.request.task;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FindTaskByPerformerReqDTO {

    @NotBlank
    private String performerEmail;
}
