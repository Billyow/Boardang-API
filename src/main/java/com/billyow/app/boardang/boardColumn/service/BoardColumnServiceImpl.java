package com.billyow.app.boardang.boardColumn.service;

import com.billyow.app.boardang.auth.service.AuthService;
import com.billyow.app.boardang.board.model.Board;
import com.billyow.app.boardang.board.repository.IBoardRepository;
import com.billyow.app.boardang.boardColumn.DTO.BoardColumnCreateRequest;
import com.billyow.app.boardang.boardColumn.mapper.BoardColumnMapper;
import com.billyow.app.boardang.boardColumn.model.BoardColumn;
import com.billyow.app.boardang.boardColumn.repository.IBoardColumnRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
@Service
@AllArgsConstructor
public class BoardColumnServiceImpl implements IBoardColumnService{
    private final IBoardRepository boardRepository;
    private final AuthService authService;
    private final BoardColumnMapper boardColumnMapper;
    private final IBoardColumnRepository boardColumnRepository;
    @Override
    public void createColumn(BoardColumnCreateRequest request){
        var board = boardRepository.findById(request.boardId()).orElseThrow(() -> new RuntimeException("Board not found"));
        var currentUser = authService.getCurrentUserId();
        validateUserCanManageColumns(board, currentUser);

        //calculate the next position
        int nextPosition = board.getColumns()
                .stream()
                .map(BoardColumn::getPosition)
                .max(Comparator.naturalOrder())
                .orElse(0)+1;

        var boardEntity = boardColumnMapper.toBoardColumn(request, board, nextPosition);
        boardColumnRepository.save(boardEntity);

    }

    @Override
    public List<BoardColumn> getColumnsByBoard(Long boardId) {
        return List.of();
    }

    @Override
    public void deleteColumn(Long columnId) {

    }

    @Override
    public BoardColumn updateColumn(String columnTitle, Long boardColumnId) {
        return null;
    }

    @Override
    public Integer getColumnCountByBoardId(Long boardId) {
        return 0;
    }

    private void validateUserCanManageColumns(Board board, Long currentUserId){
        var isOwner = board.getOwner().getId().equals(currentUserId);

        var isMember = board.getMembers()
                .stream()
                .anyMatch(member -> member.getId().equals(currentUserId));
        if(!isOwner && !isMember){
            throw new RuntimeException("You are not part of this board");
        }
    }
}
