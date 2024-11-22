package veniamin.tasksapp.backend.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import veniamin.tasksapp.backend.dto.request.task.TaskCreateReqDTO;
import veniamin.tasksapp.backend.dto.request.task.TaskUpdateReqDTO;
import veniamin.tasksapp.backend.entity.Task;

import java.util.List;

public interface TaskService {

    void createTask(@Valid TaskCreateReqDTO createTaskDTO, HttpServletRequest request);

    void updateTask(@Valid TaskUpdateReqDTO updateTaskDTO, HttpServletRequest request);

    List<Task> getAllTask();
    List<Task> getTask(Long amount, HttpServletRequest request);
}
