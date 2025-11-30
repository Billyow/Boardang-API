package com.billyow.app.boardang.board.controller;

import com.billyow.app.boardang.board.DTO.BoardResponse;
import com.billyow.app.boardang.board.DTO.BoardSummaryResponse;
import com.billyow.app.boardang.board.DTO.CreateBoardRequest;
import com.billyow.app.boardang.board.service.IBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/board")
@RequiredArgsConstructor
@Controller
public class BoardController {
    private final IBoardService boardService;
    @PostMapping("/create")
    public ResponseEntity<BoardResponse> createBoard(@RequestBody CreateBoardRequest request) {
        return ResponseEntity.ok(boardService.createBoard(request));
    }
    @GetMapping("/getAllMyBoards")
    public ResponseEntity<List<BoardSummaryResponse>> getBoards() {
        return ResponseEntity.ok(boardService.getCurrentUserBoards());
    }

    @DeleteMapping("delete/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build();
    }

     @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.getBoard(boardId));
     }
}
