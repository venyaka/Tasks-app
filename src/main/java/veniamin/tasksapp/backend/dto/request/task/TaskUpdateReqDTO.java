package veniamin.tasksapp.backend.dto.request.task;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskUpdateReqDTO {

    @NotBlank
    private Long id;

    @NotBlank
    private String title;

    private String details;

    private String comment;

    private String priority;

    private LocalDate date;

    private String performerEmail;

    @NotBlank
    private String creatorEmail;
}
