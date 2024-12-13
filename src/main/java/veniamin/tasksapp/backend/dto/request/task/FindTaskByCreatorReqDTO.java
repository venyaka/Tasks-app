package veniamin.tasksapp.backend.dto.request.task;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FindTaskByCreatorReqDTO {

    @NotBlank
    private String creatorEmail;
}
