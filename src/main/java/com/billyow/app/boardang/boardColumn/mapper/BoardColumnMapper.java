package com.billyow.app.boardang.boardColumn.mapper;
import com.billyow.app.boardang.board.model.Board;
import com.billyow.app.boardang.boardColumn.DTO.BoardColumnCreateRequest;
import com.billyow.app.boardang.boardColumn.DTO.BoardColumnResponse;
import com.billyow.app.boardang.boardColumn.model.BoardColumn;
import com.billyow.app.boardang.task.DTO.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BoardColumnMapper {
    @Mapping(target = "tasks", source = "tasks")
    BoardColumnResponse toResponse(BoardColumn boardColumn, List<TaskResponse> tasks);

    // assign the board on the service manually to prevent looping
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "position", source = "position")
    @Mapping(target = "board", source = "board")
    @Mapping(target = "title" , source = "request.title")
    BoardColumn toBoardColumn(BoardColumnCreateRequest request, Board board, Double position);
}
