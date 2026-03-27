package com.billyow.app.boardang.board.DTO;

import com.billyow.app.boardang.user.DTO.SimpleUserDTO;

public record BoardMemberResponse(SimpleUserDTO user, String role) {
}
