package com.billyow.app.boardang.board.controller;

import com.billyow.app.boardang.board.DTO.CreateBoardRequest;
import com.billyow.app.boardang.board.model.Board;
import com.billyow.app.boardang.board.service.IBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
public class BoardController {
    private final IBoardService boardService;
    @PostMapping("/boards")
    public ResponseEntity<Board> createBoard(@RequestBody CreateBoardRequest request) {
        return ResponseEntity.ok(boardService.createBoard(request));
    }
}
