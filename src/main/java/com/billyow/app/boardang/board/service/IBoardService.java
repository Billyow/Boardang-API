package com.billyow.app.boardang.board.service;

import com.billyow.app.boardang.board.DTO.BoardResponse;
import com.billyow.app.boardang.board.DTO.BoardSummaryResponse;
import com.billyow.app.boardang.board.DTO.CreateBoardRequest;
import com.billyow.app.boardang.user.DTO.SimpleUserDTO;

import java.util.List;
import java.util.Set;

public interface IBoardService {
    BoardResponse createBoard(CreateBoardRequest request);
    List<BoardSummaryResponse> getCurrentUserBoards();
    BoardResponse getBoard(Long boardId);
    void deleteBoard(Long boardId);
    Set<SimpleUserDTO> getMembers(Long boardId);
    Set<SimpleUserDTO> addMember(Long boardId, String email);
    void removeMember(Long boardId, Long userId);
}
