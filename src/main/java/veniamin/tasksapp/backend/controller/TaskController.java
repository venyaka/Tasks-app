package veniamin.tasksapp.backend.controller;

import org.springframework.web.bind.annotation.*;
import veniamin.tasksapp.backend.constant.PathConstants;
import veniamin.tasksapp.backend.dto.request.task.TaskUpdateReqDTO;
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
    public void createTask(@Valid @RequestBody TaskCreateReqDTO createTaskDTO, HttpServletRequest request){
        taskService.createTask(createTaskDTO, request);
    }

    @PostMapping("/edit")
    public void updateTask(@Valid @RequestBody TaskUpdateReqDTO updateTaskDTO, HttpServletRequest request){
        taskService.updateTask(updateTaskDTO, request);
    }

    @GetMapping()
    public List<Task> getAllTask(){
        return taskService.getAllTask();
    }

    @GetMapping("/{task}")
    public List<Task> getTask(@PathVariable("task") Long amount, HttpServletRequest request){
        return taskService.getTask(amount, request);
    }
}
