package veniamin.tasksapp.backend.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import veniamin.tasksapp.backend.entity.User;

import java.time.LocalDate;

@Data
public class TaskRespDTO {

    private Long id;

    private String title;

    private String details;

    private LocalDate date;

    @NotNull
    private Boolean isComplete;

    private String priority;

    private String comment;

    private String creatorEmail;

    private String performerEmail;
}
