package veniamin.tasksapp.backend.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import veniamin.tasksapp.backend.dto.request.task.TaskCreateReqDTO;
import veniamin.tasksapp.backend.dto.request.task.TaskUpdateReqDTO;
import veniamin.tasksapp.backend.entity.Task;
import veniamin.tasksapp.backend.entity.User;
import veniamin.tasksapp.backend.exception.BadRequestException;
import veniamin.tasksapp.backend.exception.errors.AuthorizedError;
import veniamin.tasksapp.backend.exception.errors.BadRequestError;
import veniamin.tasksapp.backend.repository.TaskRepository;
import veniamin.tasksapp.backend.repository.UserRepository;
import veniamin.tasksapp.backend.service.TaskService;
import veniamin.tasksapp.backend.service.UserService;
import veniamin.tasksapp.backend.utils.LogsUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final UserRepository userRepository;

    private final TaskRepository taskRepository;

    private final LogsUtils logsUtils;

    private static final Logger loggerAuth = LoggerFactory.getLogger("authLogger");


    @Override
    public void createTask(TaskCreateReqDTO createTaskDTO, HttpServletRequest request) {
        Optional<User> optionalCreator = userRepository.findByEmail(createTaskDTO.getCreatorEmail());
        if (optionalCreator.isEmpty()) {
            throw new BadCredentialsException(AuthorizedError.USER_WITH_THIS_EMAIL_NOT_FOUND.getMessage());
        }

        Optional<User> optionalPerformer = userRepository.findByEmail(createTaskDTO.getPerformerEmail());
        if (optionalPerformer.isEmpty()) {
            throw new BadCredentialsException(AuthorizedError.USER_WITH_THIS_EMAIL_NOT_FOUND.getMessage());
        }

        User creator = optionalCreator.get();
        User performer = optionalPerformer.get();

        Task task = new Task();
        task.setTitle(createTaskDTO.getTitle());
        task.setDetails(createTaskDTO.getDetails());
        task.setComment(createTaskDTO.getComment());
        task.setPriority(createTaskDTO.getPriority());
        task.setDate(createTaskDTO.getDate());
        task.setStatus(Boolean.FALSE);
        task.setPerformer(performer);
        task.setCreator(creator);
        taskRepository.save(task);

        logsUtils.log(loggerAuth, "Create task - " + createTaskDTO.getTitle());
    }

    @Override
    public void updateTask(TaskUpdateReqDTO updateTaskDTO, HttpServletRequest request) {

        Optional<Task> optionalTask = taskRepository.findById(updateTaskDTO.getId());
        if (optionalTask.isEmpty()) {
            throw new BadCredentialsException(AuthorizedError.TASK_NOT_FOUND.getMessage());
        }

        Optional<User> optionalCreator = userRepository.findByEmail(updateTaskDTO.getCreatorEmail());
        if (optionalCreator.isEmpty()) {
            throw new BadCredentialsException(AuthorizedError.USER_WITH_THIS_EMAIL_NOT_FOUND.getMessage());
        }

        Optional<User> optionalPerformer = userRepository.findByEmail(updateTaskDTO.getPerformerEmail());
        if (optionalPerformer.isEmpty()) {
            throw new BadCredentialsException(AuthorizedError.USER_WITH_THIS_EMAIL_NOT_FOUND.getMessage());
        }

        User creator = optionalCreator.get();
        User performer = optionalPerformer.get();

        Task task = optionalTask.get();
        task.setTitle(updateTaskDTO.getTitle());
        task.setDetails(updateTaskDTO.getDetails());
        task.setComment(updateTaskDTO.getComment());
        task.setPriority(updateTaskDTO.getPriority());
        task.setDate(updateTaskDTO.getDate());
        task.setPerformer(performer);
        task.setCreator(creator);
        taskRepository.save(task);

        logsUtils.log(loggerAuth, "Create task - " + updateTaskDTO.getTitle());
    }

    @Override
    public List<Task> getAllTask() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> getTask(Long amount, HttpServletRequest request) {
        return taskRepository.findAll();
    }
}
