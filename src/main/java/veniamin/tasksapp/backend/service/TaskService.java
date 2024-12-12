package veniamin.tasksapp.backend.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import veniamin.tasksapp.backend.dto.request.task.TaskCommentChangeReqDTO;
import veniamin.tasksapp.backend.dto.request.task.TaskCreateReqDTO;
import veniamin.tasksapp.backend.dto.request.task.TaskStatusChangeReqDTO;
import veniamin.tasksapp.backend.dto.request.task.TaskUpdateReqDTO;
import veniamin.tasksapp.backend.dto.response.TaskRespDTO;
import veniamin.tasksapp.backend.entity.Task;

import java.util.List;

public interface TaskService {

    void createTask(@Valid TaskCreateReqDTO createTaskDTO, HttpServletRequest request);

    void updateTask(@Valid TaskUpdateReqDTO updateTaskDTO, HttpServletRequest request);

    void updateTaskStatus(@Valid TaskStatusChangeReqDTO updateTaskStatusDTO, HttpServletRequest request);

    void updateTaskComment(@Valid TaskCommentChangeReqDTO updateTaskCommentDTO, HttpServletRequest request);

    Page<TaskRespDTO> findAllTask(Pageable pageable);
    List<Task> getTask(Long amount, HttpServletRequest request);
}
