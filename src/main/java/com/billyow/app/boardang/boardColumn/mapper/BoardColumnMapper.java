package com.billyow.app.boardang.boardColumn.mapper;
import com.billyow.app.boardang.boardColumn.DTO.BoardColumnCreateRequest;
import com.billyow.app.boardang.boardColumn.DTO.BoardColumnResponse;
import com.billyow.app.boardang.boardColumn.model.BoardColumn;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BoardColumnMapper {
    //get the boardId from board.id
    @Mapping(target = "boardId", source = "board.id")
    BoardColumnResponse toResponse(BoardColumn boardColumn);

    // assign the board on the service manually to prevent looping
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "board", ignore = true)
    BoardColumn toBoardColumn(BoardColumnCreateRequest request);
}
