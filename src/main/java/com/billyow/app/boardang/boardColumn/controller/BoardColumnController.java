package com.billyow.app.boardang.boardColumn.controller;

import com.billyow.app.boardang.boardColumn.DTO.BoardColumnCreateRequest;
import com.billyow.app.boardang.boardColumn.service.IBoardColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/boards/{boardId}/columns")
public class BoardColumnController {

    private final IBoardColumnService boardColumnService;

    @PostMapping
    public ResponseEntity<Void> createColumn(
            @PathVariable Long boardId,
            @RequestBody BoardColumnCreateRequest request
    ) {
        BoardColumnCreateRequest adjustedRequest = new BoardColumnCreateRequest(
                boardId,
                request.title()
        );

        boardColumnService.createColumn(adjustedRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
