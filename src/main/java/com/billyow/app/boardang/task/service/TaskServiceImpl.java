package com.billyow.app.boardang.task.service;

import com.billyow.app.boardang.auth.service.AuthService;
import com.billyow.app.boardang.board.model.Board;
import com.billyow.app.boardang.board.repository.IBoardRepository;
import com.billyow.app.boardang.boardColumn.model.BoardColumn;
import com.billyow.app.boardang.boardColumn.repository.IBoardColumnRepository;
import com.billyow.app.boardang.task.DTO.CreateTaskRequest;
import com.billyow.app.boardang.task.DTO.MoveTaskRequest;
import com.billyow.app.boardang.task.DTO.TaskResponse;
import com.billyow.app.boardang.task.assembler.TaskResponseAssembler;
import com.billyow.app.boardang.task.mapper.TaskMapper;
import com.billyow.app.boardang.task.model.Task;
import com.billyow.app.boardang.task.repository.ITaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
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
    public void createTask(CreateTaskRequest request) {
        // validate that the board exists
        Board board = boardRepository.findById(request.boardId())
                .orElseThrow(() -> new RuntimeException("Board not found"));

        // validate that the column exists
        BoardColumn column = boardColumnRepository.findById(request.columnId())
                .orElseThrow(() -> new RuntimeException("Column not found"));

        // validate that the column belongs to the board
        if (!board.getColumns().contains(column)){
            throw new RuntimeException("Column doesn't belong to board");
        }

        Long currentUserId = authService.getCurrentUserId();
        validateUserCanManageTasks(board, currentUserId);
        Task savedTask = taskMapper.toEntity(request,currentUserId, new HashSet<>());
        taskRepository.save(savedTask);
    }

    @Override
    public void deleteByBoardId(Long boardId) {

    }

    @Override
    public void deleteByBoardColumnId(Long columnId) {

    }

    @Override
    public void deleteByTaskId(String taskId,Long boardId) {
        Task taskToDelete = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("Board not found"));
        Long currentUserId = authService.getCurrentUserId();

        //validate permissions
        validateUserCanManageTasks(board, currentUserId);

        if(!board.getId()
                .equals(taskToDelete.getBoardId())){
            throw new RuntimeException("Board doesn't belong to task");
        }

        taskRepository.deleteById(taskToDelete.getId());

    }

    @Override
    public List<TaskResponse> getTasksByBoardColumnId(Long columnId) {
        var tasks = taskRepository.getTasksByColumnId(columnId);
        return taskAssembler.convertTasksToResponse(tasks);
    }

    @Override
    public void moveTaskToColumn(MoveTaskRequest request) {
        Task task = taskRepository.getTaskById(String.valueOf(request.taskId()));

        if(!task.getBoardId().equals(request.boardId())) {
            throw new RuntimeException("Task doesn't belong to board");
        }

        //validate board and permits
        Board board = boardRepository.findById(request.boardId())
                .orElseThrow(() -> new RuntimeException("Board not found"));

        Long userId = authService.getCurrentUserId();
        validateUserCanManageTasks(board, userId);

        //validate column
        BoardColumn newColumn = boardColumnRepository.findById(
                request.newColumnId())
                .orElseThrow(() -> new RuntimeException(("column not found")));

        if(!newColumn.getBoard().getId().equals(board.getId())){
            throw new RuntimeException("board doesn't belong to column");
        }

        task.setColumnId(request.newColumnId());

        taskRepository.save(task);
    }

    private void validateUserCanManageTasks(Board board, Long currentUserId) {
        var isOwner = board.getOwner().getId().equals(currentUserId);
        var isMember = board.getMembers()
                .stream()
                .anyMatch((user) -> user.getId().equals(currentUserId));
        if (!isOwner || !isMember) {
            throw new RuntimeException("you are not part of the board");
        }
    }
}
