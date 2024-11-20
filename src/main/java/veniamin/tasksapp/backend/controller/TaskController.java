package veniamin.tasksapp.backend.controller;

import org.springframework.web.bind.annotation.*;
import veniamin.tasksapp.backend.constant.PathConstants;
import veniamin.tasksapp.backend.service.AuthorizeService;
import veniamin.tasksapp.backend.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import veniamin.tasksapp.backend.dto.request.task.CreateTaskReqDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.TASK_CONTROLLER_PATH)
public class TaskController {
    private final AuthorizeService authorizeService;
    private final TaskService taskService;

    @PostMapping
    public void createTask(@Valid @RequestBody CreateTaskReqDTO createTaskDTO, HttpServletRequest request){

    }

    @PostMapping
    public void updateTask(@Valid @RequestBody CreateTaskReqDTO createTaskDTO, HttpServletRequest request){

    }

    @GetMapping
    public void getTask(@Valid @RequestBody CreateTaskReqDTO createTaskDTO, HttpServletRequest request){

    }
}
