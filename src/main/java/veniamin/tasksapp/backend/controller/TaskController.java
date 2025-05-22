package veniamin.tasksapp.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import veniamin.tasksapp.backend.constant.PathConstants;
import veniamin.tasksapp.backend.dto.request.task.*;
import veniamin.tasksapp.backend.dto.response.TaskRespDTO;
import veniamin.tasksapp.backend.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.TASK_CONTROLLER_PATH)
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    @Operation(summary = "Получение всех задач c выборкой по автору и исполнителю")
    public Page<TaskRespDTO> getAllTask(@PageableDefault Pageable pageable){
        return taskService.findAllTasksForCurrentUser(pageable);
    }

//    @GetMapping
//    @Operation(summary = "Получение всех задач")
//    public Page<TaskRespDTO> getAllTask(@PageableDefault Pageable pageable){
//        return taskService.findTasks(pageable);
//    }


    @PostMapping
    @Operation(summary = "Создание новой задачи, досуп имеет только admin")
    public void save(@Valid @RequestBody TaskCreateReqDTO createTaskDTO){
        taskService.save(createTaskDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение одной задачи по id")
    public TaskRespDTO findById(@PathVariable("id") Long id){
        return taskService.findByIdForCurrentUser(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактирование существующей задачи, досуп имеет только admin")
    public TaskRespDTO updateTask(@PathVariable("id") Long id, @Valid @RequestBody TaskUpdateReqDTO updateTaskDTO){
        return taskService.update(updateTaskDTO, id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление существующей задачи, досуп имеет только admin")
    public void deleteById(@PathVariable("id") Long id){
        taskService.deleteById(id);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Редактирование существующей задачи, досуп имеет только admin")
    public TaskRespDTO updateById(@PathVariable("id") Long id, @Valid @RequestBody TaskUpdateReqDTO updateTaskDTO){
        return taskService.update(updateTaskDTO, id);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Изменение статуса задачи")
    public void updateStatusById(@PathVariable("id") Long id, @Valid @RequestBody Boolean status){
        taskService.updateStatus(id, status);
    }


}