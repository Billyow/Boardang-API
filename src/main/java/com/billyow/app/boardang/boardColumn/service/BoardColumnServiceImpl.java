package com.billyow.app.boardang.boardColumn.service;

import com.billyow.app.boardang.auth.service.AuthService;
import com.billyow.app.boardang.boardColumn.DTO.BoardColumnResponse;
import com.billyow.app.boardang.boardColumn.DTO.BoardColumnUpdateRequest;
import com.billyow.app.boardang.exception.BadRequestException;
import com.billyow.app.boardang.exception.ForbiddenException;
import com.billyow.app.boardang.exception.ResourceNotFoundException;
import com.billyow.app.boardang.board.model.Board;
import com.billyow.app.boardang.board.repository.IBoardRepository;
import com.billyow.app.boardang.boardColumn.DTO.BoardColumnCreateRequest;
import com.billyow.app.boardang.boardColumn.DTO.MoveColumnRequest;
import com.billyow.app.boardang.boardColumn.mapper.BoardColumnMapper;
import com.billyow.app.boardang.boardColumn.model.BoardColumn;
import com.billyow.app.boardang.boardColumn.repository.IBoardColumnRepository;
import com.billyow.app.boardang.task.assembler.TaskResponseAssembler;
import com.billyow.app.boardang.task.repository.ITaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
@AllArgsConstructor
public class BoardColumnServiceImpl implements IBoardColumnService{
    private final IBoardRepository boardRepository;
    private final AuthService authService;
    private final BoardColumnMapper boardColumnMapper;
    private final IBoardColumnRepository boardColumnRepository;
    private final ITaskRepository taskRepository;
    private final TaskResponseAssembler taskAssembler;
    @Override
    public BoardColumnResponse createColumn(BoardColumnCreateRequest request){
        var board = boardRepository.findById(request.boardId()).orElseThrow(() -> new ResourceNotFoundException("Board not found"));
        var currentUser = authService.getCurrentUserId();
        validateUserCanManageColumns(board, currentUser);

        //calculate the next position
        Double maxPosition = boardColumnRepository.getMaxPositionByBoardId(request.boardId());
        double nextPosition = (maxPosition == null ? 0.0 : maxPosition) + 1.0;

        var boardEntity = boardColumnMapper.toBoardColumn(request, board, nextPosition);
        boardColumnRepository.save(boardEntity);
        return boardColumnMapper.toResponse(boardEntity, new ArrayList<>());
    }


    @Override
    public void deleteColumn(Long columnId) {
        var column = boardColumnRepository.findById(columnId).orElseThrow(() -> new ResourceNotFoundException("Column not found"));
        var user = authService.getCurrentUserId();
        validateUserCanManageColumns(column.getBoard(), user);
        boardColumnRepository.deleteById(columnId);
    }

    @Override
    public BoardColumnResponse updateColumn(BoardColumnUpdateRequest request, Long boardColumnId) {
        var column = boardColumnRepository.findById(boardColumnId).orElseThrow(() -> new ResourceNotFoundException("Column not found"));
        var user = authService.getCurrentUserId();
        validateUserCanManageColumns(column.getBoard(), user);
        column.setTitle(request.title());
        column.setPosition(request.position());
        boardColumnRepository.save(column);
        var tasks = taskRepository.getTasksByColumnId(boardColumnId);
        var taskResponses = taskAssembler.convertTasksToResponse(tasks);
        return boardColumnMapper.toResponse(column, taskResponses);
    }

    @Override
    public Integer getColumnCountByBoardId(Long boardId) {
        return boardColumnRepository.countByBoard_Id(boardId);
    }

    @Override
    public BoardColumnResponse moveColumn(Long columnId, Long boardId, MoveColumnRequest request) {
        if (request.afterColumnId() == null && request.beforeColumnId() == null) {
            throw new BadRequestException("At least one of afterColumnId or beforeColumnId must be provided");
        }

        var column = boardColumnRepository.findById(columnId).orElseThrow(() -> new ResourceNotFoundException("Column not found"));
        validateUserCanManageColumns(column.getBoard(), authService.getCurrentUserId());

        Double afterPosition = null;
        Double beforePosition = null;

        if (request.afterColumnId() != null) {
            afterPosition = boardColumnRepository.findById(request.afterColumnId())
                    .orElseThrow(() -> new ResourceNotFoundException("After column not found"))
                    .getPosition();
        }

        if (request.beforeColumnId() != null) {
            beforePosition = boardColumnRepository.findById(request.beforeColumnId())
                    .orElseThrow(() -> new ResourceNotFoundException("Before column not found"))
                    .getPosition();
        }

        double newPosition;
        if (afterPosition != null && beforePosition != null) {
            newPosition = (afterPosition + beforePosition) / 2.0;
        } else if (afterPosition != null) {
            newPosition = afterPosition + 1.0;
        } else {
            newPosition = beforePosition - 1.0;
        }

        column.setPosition(newPosition);
        boardColumnRepository.save(column);
        var tasks = taskRepository.getTasksByColumnId(columnId);
        var taskResponses = taskAssembler.convertTasksToResponse(tasks);
        return boardColumnMapper.toResponse(column, taskResponses);
    }

    private void validateUserCanManageColumns(Board board, Long currentUserId){
        var isOwner = board.getOwner().getId().equals(currentUserId);

        var isMember = board.getMembers()
                .stream()
                .anyMatch(member -> member.getId().equals(currentUserId));
        if(!isOwner && !isMember){
            throw new ForbiddenException("You are not part of this board");
        }
    }
}
