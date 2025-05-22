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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
//import veniamin.tasksapp.backend.configuration.mapstruct.TaskToTaskRespDTO;
import veniamin.tasksapp.backend.configuration.mapstruct.TaskToTaskRespDTO;
import veniamin.tasksapp.backend.dto.request.task.*;
import veniamin.tasksapp.backend.dto.response.TaskRespDTO;
import veniamin.tasksapp.backend.entity.Role;
import veniamin.tasksapp.backend.entity.Task;
import veniamin.tasksapp.backend.entity.User;
import veniamin.tasksapp.backend.exception.BadRequestException;
import veniamin.tasksapp.backend.exception.NotFoundException;
import veniamin.tasksapp.backend.exception.errors.AuthorizedError;
import veniamin.tasksapp.backend.exception.errors.BadRequestError;
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
    private static final Logger taskLogger = LoggerFactory.getLogger("taskLogger");

    private final TaskToTaskRespDTO taskToTaskRespDTO;

    @Override
    @Transactional
    public TaskRespDTO save(TaskCreateReqDTO createTaskDTO) {
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

        logsUtils.log(taskLogger, "Create task - " + createTaskDTO.getTitle());
        return getRespDTO(task);
    }

    @Override
    public TaskRespDTO update(TaskUpdateReqDTO updateTaskDTO, Long taskId) {

        Optional<Task> optionalTask = taskRepository.findById(taskId);
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

        logsUtils.log(taskLogger, "Update task - " + updateTaskDTO.getTitle());

        return getRespDTO(task);
    }

    @Override
    public void updateStatus(Long taskId, Boolean status) {

        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isEmpty()) {
            throw new BadCredentialsException(AuthorizedError.TASK_NOT_FOUND.getMessage());
        }

        Task task = optionalTask.get();
        task.setIsComplete(status);
        taskRepository.save(task);

        logsUtils.log(taskLogger, "Update status task - " + task.getIsComplete());
    }


    @Override
    @Transactional
    public void deleteById(Long courseId) {
        Task task = taskRepository.findById(courseId).orElseThrow(() -> new NotFoundException(NotFoundError.TASK_NOT_FOUND));
        if (!isAvailable(task)) {
            throw new BadRequestException(BadRequestError.TASK_NOT_BELONG_TO_THIS_USER);
        }
        taskRepository.delete(task);
        logsUtils.log(taskLogger, "Delete course (id " + courseId + ")");
    }

    @Override
    @Transactional
    public Page<TaskRespDTO> findAll(Pageable pageable) {
        List<Task> tasks = taskRepository.findAll()
                .stream().toList();

        Page<Task> page = new PageImpl<>(tasks, pageable, tasks.size());

        return page.map(taskToTaskRespDTO::sourceToDestination);
    }

    @Override
    @Transactional
    public Page<TaskRespDTO> findAllTasksForCurrentUser(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = authentication.getName().equals("anonymousUser") ? new User() : userRepository.findByEmail(authentication.getName()).get();

        List<Task> tasks = taskRepository.findAll()
                .stream().filter(m -> m.getPerformer().getEmail().equals(user.getEmail()) || m.getCreator().getEmail().equals(user.getEmail())).toList();

        Page<Task> page = new PageImpl<>(tasks, pageable, tasks.size());

        return page.map(taskToTaskRespDTO::sourceToDestination);
    }

    @Override
    @Transactional
    public TaskRespDTO findByIdForCurrentUser(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException(NotFoundError.TASK_NOT_FOUND));
        User user = getCurrentUser();
        if (!task.getPerformer().getEmail().equals(user.getEmail()) && !task.getCreator().getEmail().equals(user.getEmail())) {
            throw new NotFoundException(NotFoundError.TASK_NOT_FOUND);
        }

        return getRespDTO(task);
    }

    @Override
    @Transactional
    public TaskRespDTO findById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException(NotFoundError.TASK_NOT_FOUND));

        return getRespDTO(task);
    }

    private boolean isAvailable(Task course) {
        User user = getCurrentUser();

        boolean isAdmin = user.getRoles().contains(Role.ADMIN);

        if (isAdmin) return true;

        return false;
    }

    @Override
    public TaskRespDTO getRespDTO(Task task) {
        TaskRespDTO taskRespDTO = new TaskRespDTO();
        taskRespDTO.setComment(task.getComment());
        taskRespDTO.setId(task.getId());
        taskRespDTO.setDate(task.getDate());
        taskRespDTO.setDetails(task.getDetails());
        taskRespDTO.setPriority(task.getPriority());
        taskRespDTO.setIsComplete(task.getIsComplete());
        taskRespDTO.setCreatorEmail(task.getCreator().getEmail());
        taskRespDTO.setPerformerEmail(task.getPerformer().getEmail());
        taskRespDTO.setTitle(task.getTitle());

        return taskRespDTO;
    }

    @Transactional
    protected User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(NotFoundError.USER_NOT_FOUND));
    }
}
