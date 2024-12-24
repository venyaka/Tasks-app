package veniamin.tasksapp.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import veniamin.tasksapp.backend.constant.PathConstants;
import veniamin.tasksapp.backend.dto.request.task.*;
import veniamin.tasksapp.backend.dto.response.TaskRespDTO;
import veniamin.tasksapp.backend.entity.Task;
import veniamin.tasksapp.backend.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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

    @PatchMapping("/edit-status")
    @Operation(summary = "Редактирование статуса задачи, досуп имеет как admin так и пользователь")
    public void updateTaskStatus(@Valid @RequestBody TaskStatusChangeReqDTO updateTaskStatusDTO, HttpServletRequest request){
        taskService.updateTaskStatus(updateTaskStatusDTO, request);
    }

    @PatchMapping("/edit-comment")
    @Operation(summary = "Редактирование комментария к задаче, досуп имеет как admin так и пользователь")
    public void updateTaskComment(@Valid @RequestBody TaskCommentChangeReqDTO updateTaskCommentDTO, HttpServletRequest request){
        taskService.updateTaskComment(updateTaskCommentDTO, request);
    }

    @GetMapping()
    @Operation(summary = "Получение всех задач")
    public Page<TaskRespDTO> getAllTask(@PageableDefault Pageable pageable){
        return taskService.findAllTask(pageable);
    }

    @GetMapping("/get-task-by-creator")
    @Operation(summary = "Получение всех задач")
    public Page<TaskRespDTO> getAllTaskByCreator(@Valid @RequestBody FindTaskByCreatorReqDTO findTaskDTO, @PageableDefault Pageable pageable){
        return taskService.findAllTaskByCreator(findTaskDTO, pageable);
    }

    @GetMapping("/get-task-by-performer")
    @Operation(summary = "Получение всех задач")
    public Page<TaskRespDTO> getAllTaskByPerformer(@Valid @RequestBody FindTaskByPerformerReqDTO findTaskDTO, @PageableDefault Pageable pageable){
        return taskService.findAllTaskByPerformer(findTaskDTO, pageable);
    }

    @GetMapping("/{creator}/{performer}")
    @Operation(summary = "Получение всех задач c выборкой по автору и исполнителю")
    public Page<TaskRespDTO> getTasks(@PathVariable("creator") String creator, @PathVariable("performer") String performer, @PageableDefault Pageable pageable){
        return taskService.findTasks(creator, performer, pageable);
    }

    @GetMapping("/{task}")
    @Operation(summary = "Получение одной задачи по id")
    public List<Task> getTask(@PathVariable("task") Long amount, HttpServletRequest request){
        return taskService.getTask(amount, request);
    }
}