package veniamin.tasksapp.backend.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import veniamin.tasksapp.backend.dto.request.task.*;
import veniamin.tasksapp.backend.dto.response.TaskRespDTO;
import veniamin.tasksapp.backend.entity.Task;

import java.util.List;

public interface TaskService {

    TaskRespDTO save(@Valid TaskCreateReqDTO createTaskDTO);

    TaskRespDTO update(@Valid TaskUpdateReqDTO updateTaskDTO, Long taskId);

    void deleteById(Long taskId);

    Page<TaskRespDTO> findAll(Pageable pageable);

    Page<TaskRespDTO> findAllTasksForCurrentUser(Pageable pageable);

    TaskRespDTO findById(Long taskId);

    TaskRespDTO findByIdForCurrentUser(Long taskId);

    void updateStatus(Long taskId, Boolean status);

    TaskRespDTO getRespDTO(Task task);

}
