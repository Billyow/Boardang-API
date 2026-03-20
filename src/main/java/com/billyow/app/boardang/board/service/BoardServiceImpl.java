package com.billyow.app.boardang.board.service;

import com.billyow.app.boardang.auth.service.AuthService;
import com.billyow.app.boardang.exception.ForbiddenException;
import com.billyow.app.boardang.exception.ResourceNotFoundException;
import com.billyow.app.boardang.board.DTO.BoardResponse;
import com.billyow.app.boardang.board.DTO.BoardSummaryResponse;
import com.billyow.app.boardang.board.DTO.CreateBoardRequest;
import com.billyow.app.boardang.board.mapper.BoardMapper;
import com.billyow.app.boardang.board.model.Board;
import com.billyow.app.boardang.board.repository.IBoardRepository;
import com.billyow.app.boardang.boardColumn.mapper.BoardColumnMapper;
import com.billyow.app.boardang.boardColumn.repository.IBoardColumnRepository;
import com.billyow.app.boardang.task.assembler.TaskResponseAssembler;
import com.billyow.app.boardang.task.mapper.TaskMapper;
import com.billyow.app.boardang.task.model.Task;
import com.billyow.app.boardang.task.repository.ITaskRepository;
import com.billyow.app.boardang.user.DTO.SimpleUserDTO;
import com.billyow.app.boardang.user.mapper.UserMapper;
import com.billyow.app.boardang.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements IBoardService {
    private final IBoardRepository boardRepository;
    private final AuthService authService;
    private final ITaskRepository taskRepository;
    private final IBoardColumnRepository columnRepository;
    private final IUserRepository userRepository;
    private final BoardMapper boardMapper;
    private final UserMapper userMapper;
    private final BoardColumnMapper boardColumnMapper;
    private final TaskMapper taskMapper;
    private final TaskResponseAssembler taskAssembler;

    @Transactional(readOnly = true)
    @Override
    public List<BoardSummaryResponse> getCurrentUserBoards() {
        var currentUser = authService.getCurrentUserId();
        var boards = boardRepository.findAllBoardsFromUser_Id(currentUser);
        return boards.stream()
                .map(boardMapper::toSummaryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public BoardResponse getBoard(Long boardId) {
        var board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found"));

        // 1) load all the tasks at once
        var tasks = taskRepository.getTasksByBoardId(boardId);

        // 2) group by columnId
        var tasksByColumnId = tasks.stream()
                .collect(Collectors.groupingBy(Task::getColumnId));

        var ownerResponse = userMapper.toSimpleUserDTOResponse(board.getOwner());

        var memberResponses = board.getMembers().stream()
                .map(userMapper::toSimpleUserDTOResponse)
                .collect(Collectors.toSet());

        var columnResponses = board.getColumns().stream()
                .map(column -> {
                    var columnTasks = tasksByColumnId.getOrDefault(column.getId(), List.of());
                    var tasksResponse = taskAssembler.convertTasksToResponse(columnTasks);
                    return boardColumnMapper.toResponse(column, tasksResponse);
                })
                .toList();

        return boardMapper.toResponse(
                board,
                ownerResponse,
                memberResponses,
                columnResponses
        );
    }



    @Transactional
    @Override
    public BoardResponse createBoard(CreateBoardRequest request) {
        //create the board entity
        var newBoard = new Board();
        newBoard.setTitle(request.title());
        newBoard.setDescription(request.description());
        var currentUserId = authService.getCurrentUserId();
        var owner = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        newBoard.setOwner(owner);
        newBoard.getMembers().add(owner);
        boardRepository.save(newBoard);
        var ownerResponse = userMapper.toSimpleUserDTOResponse(owner);
        //use the mappers to convert the entity into response
        return boardMapper.toResponse(newBoard,
                ownerResponse,
                new HashSet<>(),
                new ArrayList<>()
                );
    }

    @Transactional(readOnly = true)
    @Override
    public Set<SimpleUserDTO> getMembers(Long boardId) {
        var board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found"));
        return board.getMembers().stream()
                .map(userMapper::toSimpleUserDTOResponse)
                .collect(Collectors.toSet());
    }

    @Transactional
    @Override
    public Set<SimpleUserDTO> addMember(Long boardId, String email) {
        var board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found"));
        var currentUserId = authService.getCurrentUserId();
        if (!board.getOwner().getId().equals(currentUserId)) {
            throw new ForbiddenException("Only the board owner can add members");
        }
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        board.getMembers().add(user);
        boardRepository.save(board);
        return board.getMembers().stream()
                .map(userMapper::toSimpleUserDTOResponse)
                .collect(Collectors.toSet());
    }

    @Transactional
    @Override
    public void removeMember(Long boardId, Long userId) {
        var board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found"));
        var currentUserId = authService.getCurrentUserId();
        if (!board.getOwner().getId().equals(currentUserId)) {
            throw new ForbiddenException("Only the board owner can remove members");
        }
        if (board.getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Cannot remove the board owner");
        }
        board.getMembers().removeIf(m -> m.getId().equals(userId));
        boardRepository.save(board);
    }

    @Transactional
    @Override
    public void deleteBoard(Long boardId) {
        Long currentUserId = authService.getCurrentUserId();
        var affected = boardRepository.deleteByIdAndOwnerId(boardId,currentUserId);
        if(affected==1){
                columnRepository.deleteByBoard_Id(boardId);
                taskRepository.deleteTaskByBoardId(boardId);
            return;
        }
        // differentiate 403 from 404
        if(boardRepository.existsById(boardId)){
            throw new ForbiddenException("User is not the owner of this board");
        }
        throw new ResourceNotFoundException("Board not found");
    }

}
