package com.billyow.app.boardang.boardColumn.service;

import com.billyow.app.boardang.boardColumn.DTO.BoardColumnCreateRequest;

import com.billyow.app.boardang.boardColumn.DTO.BoardColumnUpdateRequest;


public interface IBoardColumnService {
    void createColumn(BoardColumnCreateRequest request);
    void deleteColumn(Long columnId);
    void updateColumn(BoardColumnUpdateRequest request, Long boardColumnId);
    Integer getColumnCountByBoardId(Long boardId);
}
