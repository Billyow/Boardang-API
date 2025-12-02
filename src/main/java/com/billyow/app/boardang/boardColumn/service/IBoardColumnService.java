package com.billyow.app.boardang.boardColumn.service;

import com.billyow.app.boardang.boardColumn.DTO.BoardColumnCreateRequest;

import com.billyow.app.boardang.boardColumn.model.BoardColumn;


public interface IBoardColumnService {
    void createColumn(BoardColumnCreateRequest request);
    void deleteColumn(Long columnId);
    BoardColumn updateColumn(String columnTitle, Long boardColumnId);
    Integer getColumnCountByBoardId(Long boardId);
}
