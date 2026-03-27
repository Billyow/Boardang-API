package com.billyow.app.boardang.board.DTO;

import com.billyow.app.boardang.boardColumn.DTO.BoardColumnResponse;
import com.billyow.app.boardang.user.DTO.SimpleUserDTO;
import java.util.Date;
import java.util.List;
import java.util.Set;

public record BoardResponse(
        Long id,
        String title,
        String description,
        Date createdAt,
        Date updatedAt,
        SimpleUserDTO owner,
        List<BoardColumnResponse> columns,
        Set<BoardMemberResponse> members
) {
}
