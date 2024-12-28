package veniamin.tasksapp.backend.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
//import veniamin.tasksapp.backend.configuration.mapstruct.TaskToTaskRespDTO;
import veniamin.tasksapp.backend.configuration.mapstruct.TaskToTaskRespDTO;
import veniamin.tasksapp.backend.dto.request.task.*;
import veniamin.tasksapp.backend.dto.response.TaskRespDTO;
import veniamin.tasksapp.backend.entity.Task;
import veniamin.tasksapp.backend.entity.User;
import veniamin.tasksapp.backend.exception.NotFoundException;
import veniamin.tasksapp.backend.exception.errors.AuthorizedError;
import veniamin.tasksapp.backend.exception.errors.NotFoundError;
import veniamin.tasksapp.backend.repository.TaskRepository;
import veniamin.tasksapp.backend.repository.UserRepository;
import veniamin.tasksapp.backend.service.TaskService;
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

    private final TaskToTaskRespDTO taskToTaskRespDTO;

    @Override
    @Transactional
    public void createTask(TaskCreateReqDTO createTaskDTO, HttpServletRequest request) {
        User creator = getCurrentUser();

        Optional<User> optionalPerformer = userRepository.findByEmail(createTaskDTO.getPerformerEmail());
        if (optionalPerformer.isEmpty()) {
            throw new BadCredentialsException(AuthorizedError.USER_WITH_THIS_EMAIL_NOT_FOUND.getMessage());
        }

        User performer = optionalPerformer.get();

        Task task = new Task();
        task.setTitle(createTaskDTO.getTitle());
        task.setDetails(createTaskDTO.getDetails());
        task.setComment(createTaskDTO.getComment());
        task.setPriority(createTaskDTO.getPriority());
        task.setDate(createTaskDTO.getDate());
        task.setIsComplete(Boolean.FALSE);
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

        Task task = optionalTask.get();
        if (updateTaskDTO.getTitle() != null) task.setTitle(updateTaskDTO.getTitle());
        if (updateTaskDTO.getDetails() != null) task.setDetails(updateTaskDTO.getDetails());
        if (updateTaskDTO.getComment() != null) task.setComment(updateTaskDTO.getComment());
        if (updateTaskDTO.getPriority() != null) task.setPriority(updateTaskDTO.getPriority());
        if (updateTaskDTO.getDate() != null) task.setDate(updateTaskDTO.getDate());

        taskRepository.save(task);

        if (updateTaskDTO.getPerformerEmail() != null) {
            Optional<User> optionalPerformer = userRepository.findByEmail(updateTaskDTO.getPerformerEmail());
            if (optionalPerformer.isEmpty()) {
                throw new BadCredentialsException(AuthorizedError.USER_WITH_THIS_EMAIL_NOT_FOUND.getMessage());
            }
            task.setPerformer(optionalPerformer.get());
        }

        taskRepository.save(task);

        logsUtils.log(loggerAuth, "Create task - " + updateTaskDTO.getTitle());
    }

    @Override
    public void updateTaskStatus(TaskStatusChangeReqDTO updateTaskStatusDTO, HttpServletRequest request) {

        Optional<Task> optionalTask = taskRepository.findById(updateTaskStatusDTO.getId());
        if (optionalTask.isEmpty()) {
            throw new BadCredentialsException(AuthorizedError.TASK_NOT_FOUND.getMessage());
        }

        Task task = optionalTask.get();
        task.setIsComplete(updateTaskStatusDTO.getIsComplete());
        taskRepository.save(task);

        logsUtils.log(loggerAuth, "Update status task - " + updateTaskStatusDTO.getIsComplete());
    }

    @Override
    public void updateTaskComment(TaskCommentChangeReqDTO updateTaskCommentDTO, HttpServletRequest request) {

        Optional<Task> optionalTask = taskRepository.findById(updateTaskCommentDTO.getId());
        if (optionalTask.isEmpty()) {
            throw new BadCredentialsException(AuthorizedError.TASK_NOT_FOUND.getMessage());
        }

        if (updateTaskCommentDTO.getComment() != null) {
            Task task = optionalTask.get();
            task.setComment(updateTaskCommentDTO.getComment());
            taskRepository.save(task);
        }

        logsUtils.log(loggerAuth, "Update comment task - " + updateTaskCommentDTO.getComment());
    }

    @Override
    @Transactional
    public Page<TaskRespDTO> findTasks(String creator, String performer, Pageable pageable) {
        List<Task> tasks = taskRepository.findAll()
                .stream().filter(m -> (m.getPerformer().getEmail().equals(performer) || performer == null) && (m.getCreator().getEmail().equals(creator) || creator == null)).toList();

        Page<Task> page = new PageImpl<>(tasks, pageable, tasks.size());

        return page.map(taskToTaskRespDTO::sourceToDestination);
    }

    @Override
    public List<Task> getTask(Long amount, HttpServletRequest request) {
        return taskRepository.findAll();
    }

    @Transactional
    protected User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(NotFoundError.USER_NOT_FOUND));
    }
}
