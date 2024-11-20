package veniamin.tasksapp.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import veniamin.tasksapp.backend.service.TaskService;
import veniamin.tasksapp.backend.service.UserService;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final UserService userService;
}
