package com.billyow.app.boardang.board.controller;

import com.billyow.app.boardang.board.DTO.AddMemberRequest;
import com.billyow.app.boardang.board.DTO.BoardResponse;
import com.billyow.app.boardang.board.DTO.BoardSummaryResponse;
import com.billyow.app.boardang.board.DTO.CreateBoardRequest;
import com.billyow.app.boardang.board.service.IBoardService;
import com.billyow.app.boardang.user.DTO.SimpleUserDTO;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@RestController
public class BoardController {
    private final IBoardService boardService;

    @PostMapping
    public ResponseEntity<BoardResponse> createBoard(@Valid @RequestBody CreateBoardRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.createBoard(request));
    }

    @GetMapping
    public ResponseEntity<List<BoardSummaryResponse>> getBoards() {
        return ResponseEntity.ok(boardService.getCurrentUserBoards());
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.getBoard(boardId));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{boardId}/members")
    public ResponseEntity<Set<SimpleUserDTO>> getMembers(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.getMembers(boardId));
    }

    @PostMapping("/{boardId}/members")
    public ResponseEntity<Set<SimpleUserDTO>> addMember(
            @PathVariable Long boardId,
            @Valid @RequestBody AddMemberRequest request
    ) {
        return ResponseEntity.ok(boardService.addMember(boardId, request.email()));
    }

    @DeleteMapping("/{boardId}/members/{userId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long boardId,
            @PathVariable Long userId
    ) {
        boardService.removeMember(boardId, userId);
        return ResponseEntity.noContent().build();
    }
}
