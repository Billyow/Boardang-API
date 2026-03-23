package com.billyow.app.boardang.boardColumn.controller;

import com.billyow.app.boardang.boardColumn.DTO.BoardColumnCreateRequest;
import com.billyow.app.boardang.boardColumn.DTO.BoardColumnResponse;
import com.billyow.app.boardang.boardColumn.DTO.BoardColumnUpdateRequest;
import com.billyow.app.boardang.boardColumn.DTO.MoveColumnRequest;
import com.billyow.app.boardang.boardColumn.service.IBoardColumnService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/boards/{boardId}/columns")
public class BoardColumnController {

    private final IBoardColumnService boardColumnService;

    @PostMapping
    public ResponseEntity<BoardColumnResponse> createColumn(
            @PathVariable Long boardId,
            @Valid @RequestBody BoardColumnCreateRequest request
    ) {
        BoardColumnCreateRequest adjustedRequest = new BoardColumnCreateRequest(
                boardId,
                request.title()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(boardColumnService.createColumn(adjustedRequest));
    }

    @DeleteMapping("/{columnId}")
    public ResponseEntity<Void> deleteColumn(@PathVariable Long columnId) {
        boardColumnService.deleteColumn(columnId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{columnId}")
    public ResponseEntity<BoardColumnResponse> updateColumn(
            @PathVariable Long columnId,
            @RequestBody BoardColumnUpdateRequest request
    ) {
        return ResponseEntity.ok(boardColumnService.updateColumn(request, columnId));
    }

    @PatchMapping("/{columnId}/move")
    public ResponseEntity<BoardColumnResponse> moveColumn(
            @PathVariable Long boardId,
            @PathVariable Long columnId,
            @RequestBody MoveColumnRequest request
    ) {
        return ResponseEntity.ok(boardColumnService.moveColumn(columnId, boardId, request));
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getColumnCount(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardColumnService.getColumnCountByBoardId(boardId));
    }
}
