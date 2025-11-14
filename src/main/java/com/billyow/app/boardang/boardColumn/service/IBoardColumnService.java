package com.billyow.app.boardang.boardColumn.service;

import com.billyow.app.boardang.boardColumn.model.BoardColumn;

import java.util.List;

public interface IBoardColumnService {
    BoardColumn create(Long boardId,String columnTitle);
    List<BoardColumn> getColumnsByBoard(Long boardId);
    void deleteColumn(Long columnId);
    BoardColumn updateColumn(String columnTitle, Long boardColumnId);
}
