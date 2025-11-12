package com.billyow.app.boardang.board.service;

import com.billyow.app.boardang.board.DTO.CreateBoardRequest;
import com.billyow.app.boardang.board.model.Board;
import java.util.List;

public interface IBoardService {
    Board createBoard(CreateBoardRequest request);
    List<Board> getUserBoards(Long userId);
    Board getBoard(Long boardId);
    void deleteBoard(Long boardId);

}
