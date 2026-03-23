package com.billyow.app.boardang.boardColumn.service;

import com.billyow.app.boardang.boardColumn.DTO.BoardColumnCreateRequest;
import com.billyow.app.boardang.boardColumn.DTO.BoardColumnResponse;
import com.billyow.app.boardang.boardColumn.DTO.BoardColumnUpdateRequest;
import com.billyow.app.boardang.boardColumn.DTO.MoveColumnRequest;


public interface IBoardColumnService {
    BoardColumnResponse createColumn(BoardColumnCreateRequest request);
    void deleteColumn(Long columnId);
    BoardColumnResponse updateColumn(BoardColumnUpdateRequest request, Long boardColumnId);
    Integer getColumnCountByBoardId(Long boardId);
    BoardColumnResponse moveColumn(Long columnId, Long boardId, MoveColumnRequest request);
}
