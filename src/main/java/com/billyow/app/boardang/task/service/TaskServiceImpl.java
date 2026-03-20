package com.billyow.app.boardang.task.service;

import com.billyow.app.boardang.auth.service.AuthService;
import com.billyow.app.boardang.exception.BadRequestException;
import com.billyow.app.boardang.exception.ForbiddenException;
import com.billyow.app.boardang.exception.ResourceNotFoundException;
import com.billyow.app.boardang.board.model.Board;
import com.billyow.app.boardang.board.repository.IBoardRepository;
import com.billyow.app.boardang.boardColumn.repository.IBoardColumnRepository;
import com.billyow.app.boardang.task.DTO.CreateTaskRequest;
import com.billyow.app.boardang.task.DTO.MoveTaskRequest;
import com.billyow.app.boardang.task.DTO.TaskResponse;
import com.billyow.app.boardang.task.DTO.UpdateTaskRequest;
import com.billyow.app.boardang.task.assembler.TaskResponseAssembler;
import com.billyow.app.boardang.task.mapper.TaskMapper;
import com.billyow.app.boardang.task.model.Task;
import com.billyow.app.boardang.task.repository.ITaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Optionals;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements ITaskService{
    private final ITaskRepository taskRepository;
    private final AuthService authService;
    private final TaskMapper taskMapper;
    private final TaskResponseAssembler taskAssembler;
    private final IBoardRepository boardRepository;
    private final IBoardColumnRepository boardColumnRepository;

    @Override
    public TaskResponse createTask(Long boardId, CreateTaskRequest request) {
        if(!boardColumnRepository.existsByIdAndBoard_Id(request.columnId(), boardId)) {
            throw new BadRequestException("Column doesn't belong to board or is not found");
        }
        var board = boardRepository.findById(boardId).orElseThrow(() -> new ResourceNotFoundException("Board not found"));
        var userId = authService.getCurrentUserId();
        validateUserCanManageTasks(board, userId);
        var task = taskMapper.toEntity(request, boardId, userId, new HashSet<>());
        taskRepository.save(task);
        return taskAssembler.convertTasksToResponse(List.of(task)).get(0);
    }

    @Override
    public void deleteByTaskId(String taskId,Long boardId) {
        Task taskToDelete = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new ResourceNotFoundException("Board not found"));
        Long currentUserId = authService.getCurrentUserId();

        //validate permissions
        validateUserCanManageTasks(board, currentUserId);

        if(!board.getId()
                .equals(taskToDelete.getBoardId())){
            throw new BadRequestException("Board doesn't belong to task");
        }

        taskRepository.deleteById(taskToDelete.getId());

    }

    @Override
    public TaskResponse updateTask(String taskId, UpdateTaskRequest request) {

        var task = Optional.of(taskRepository.getTaskById(taskId)).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        var board = boardRepository.findById(task.getBoardId()).orElseThrow(() -> new ResourceNotFoundException("Board not found"));
        validateUserCanManageTasks(board,authService.getCurrentUserId());
        if (request.title() != null) task.setTitle(request.title());
        if (request.description() != null) task.setDescription(request.description());
        if (request.priority() != null) task.setPriority(request.priority());
        taskRepository.save(task);
        return taskAssembler.convertTasksToResponse(List.of(task)).get(0);
    }

    @Override
    public List<TaskResponse> getTasksByCurrentUser() {
        Long currentUserId = authService.getCurrentUserId();
        var tasks = taskRepository.getTasksByCollaboratorsIdsContains(currentUserId);
        return taskAssembler.convertTasksToResponse(tasks);
    }

    @Override
    public TaskResponse moveTaskToColumn(String taskId, Long boardId, MoveTaskRequest request) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found"));

        Long userId = authService.getCurrentUserId();
        validateUserCanManageTasks(board, userId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if(!task.getBoardId().equals(boardId)) {
            throw new BadRequestException("Task doesn't belong to board");
        }

        if(!boardColumnRepository.existsByIdAndBoard_Id(request.newColumnId(), boardId)){
            throw new BadRequestException("Column doesn't belong to board or is not found");
        }

        task.setColumnId(request.newColumnId());
        taskRepository.save(task);
        return taskAssembler.convertTasksToResponse(List.of(task)).get(0);
    }

    @Override
    public TaskResponse assignCollaborator(String taskId, Long boardId, Long collaboratorId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found"));

        Long currentUserId = authService.getCurrentUserId();
        validateUserCanManageTasks(board, currentUserId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!task.getBoardId().equals(boardId)) {
            throw new BadRequestException("Task doesn't belong to board");
        }

        boolean isCollaboratorBoardMember = board.getMembers().stream()
                .anyMatch(m -> m.getId().equals(collaboratorId));
        if (!isCollaboratorBoardMember) {
            throw new BadRequestException("User is not a member of this board");
        }

        task.getCollaboratorsIds().add(collaboratorId);
        taskRepository.save(task);
        return taskAssembler.convertTasksToResponse(List.of(task)).get(0);
    }

    @Override
    public TaskResponse unassignCollaborator(String taskId, Long boardId, Long collaboratorId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found"));

        Long currentUserId = authService.getCurrentUserId();
        validateUserCanManageTasks(board, currentUserId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!task.getBoardId().equals(boardId)) {
            throw new BadRequestException("Task doesn't belong to board");
        }

        task.getCollaboratorsIds().remove(collaboratorId);
        taskRepository.save(task);
        return taskAssembler.convertTasksToResponse(List.of(task)).get(0);
    }

    private void validateUserCanManageTasks(Board board, Long currentUserId) {
        var isOwner = board.getOwner().getId().equals(currentUserId);
        var isMember = board.getMembers()
                .stream()
                .anyMatch((user) -> user.getId().equals(currentUserId));
        if (!isOwner && !isMember) {
            throw new ForbiddenException("you are not part of the board");
        }
    }
}
