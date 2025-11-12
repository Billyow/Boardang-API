package com.billyow.app.boardang.board.DTO;

import com.billyow.app.boardang.user.DTO.SimpleUserDTO;

public record DeleteBoardRequest(
        Long boardId,
        Long userId) {
}
