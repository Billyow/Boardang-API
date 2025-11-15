package com.billyow.app.boardang.board.DTO;

import com.billyow.app.boardang.boardColumn.DTO.BoardColumnDTO;
import com.billyow.app.boardang.user.DTO.UserDTO;
import java.util.Date;
import java.util.List;
import java.util.Set;

public record BoardResponse(
        Long id,
        String title,
        String description,
        Date createdAt,
        Date updatedAt,
        Long ownerId,
        List<BoardColumnDTO> columns,
        Set<UserDTO> members
) {
}
