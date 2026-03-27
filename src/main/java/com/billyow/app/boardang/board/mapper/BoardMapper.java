package com.billyow.app.boardang.board.mapper;

import com.billyow.app.boardang.board.DTO.BoardMemberResponse;
import com.billyow.app.boardang.board.DTO.BoardResponse;
import com.billyow.app.boardang.board.DTO.BoardSummaryResponse;
import com.billyow.app.boardang.board.model.Board;
import com.billyow.app.boardang.boardColumn.DTO.BoardColumnResponse;
import com.billyow.app.boardang.user.DTO.SimpleUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface BoardMapper {

    @Mapping(target = "id", source = "board.id")
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "members", source = "members")
    @Mapping(target = "columns", source = "columns")
    BoardResponse toResponse(Board board,
                             SimpleUserDTO owner,
                             Set<BoardMemberResponse> members,
                             List<BoardColumnResponse> columns);

    BoardSummaryResponse toSummaryResponse(Board board);
}
