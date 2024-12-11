package veniamin.tasksapp.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import veniamin.tasksapp.backend.constant.PathConstants;
import veniamin.tasksapp.backend.dto.request.task.TaskUpdateReqDTO;
import veniamin.tasksapp.backend.dto.response.TaskRespDTO;
import veniamin.tasksapp.backend.entity.Task;
import veniamin.tasksapp.backend.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import veniamin.tasksapp.backend.dto.request.task.TaskCreateReqDTO;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.TASK_CONTROLLER_PATH)
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/new")
    @Operation(summary = "Создание новой задачи, досуп имеет только admin")
    public void createTask(@Valid @RequestBody TaskCreateReqDTO createTaskDTO, HttpServletRequest request){
        taskService.createTask(createTaskDTO, request);
    }

    @PatchMapping("/edit")
    @Operation(summary = "Редактирование существующей задачи, досуп имеет только admin")
    public void updateTask(@Valid @RequestBody TaskUpdateReqDTO updateTaskDTO, HttpServletRequest request){
        taskService.updateTask(updateTaskDTO, request);
    }

    @GetMapping()
    @Operation(summary = "Получение всех задач")
    public Page<TaskRespDTO> getAllTask(@PageableDefault Pageable pageable){
        return taskService.findAllTask(pageable);
    }

    @GetMapping("/{task}")
    @Operation(summary = "Получение одной задачи по id")
    public List<Task> getTask(@PathVariable("task") Long amount, HttpServletRequest request){
        return taskService.getTask(amount, request);
    }
}