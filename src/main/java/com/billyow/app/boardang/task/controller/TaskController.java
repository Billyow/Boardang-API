package com.billyow.app.boardang.task.controller;

import com.billyow.app.boardang.task.DTO.CreateTaskRequest;
import com.billyow.app.boardang.task.DTO.MoveTaskRequest;
import com.billyow.app.boardang.task.DTO.UpdateTaskRequest;
import com.billyow.app.boardang.task.service.ITaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/boards/{boardId}/tasks")
public class TaskController {

    private final ITaskService taskService;

    @PostMapping
    public ResponseEntity<Void> createTask(
            @PathVariable Long boardId,
            @Valid @RequestBody CreateTaskRequest request
    ) {
        taskService.createTask(boardId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<Void> updateTask(
            @PathVariable String taskId,
            @RequestBody UpdateTaskRequest request
    ) {
        taskService.updateTask(taskId, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{taskId}/column")
    public ResponseEntity<Void> moveTask(
            @PathVariable Long boardId,
            @PathVariable String taskId,
            @RequestBody MoveTaskRequest request
    ) {
        taskService.moveTaskToColumn(taskId, boardId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long boardId,
            @PathVariable String taskId
    ) {
        taskService.deleteByTaskId(taskId, boardId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{taskId}/collaborators/{collaboratorId}")
    public ResponseEntity<Void> assignCollaborator(
            @PathVariable Long boardId,
            @PathVariable String taskId,
            @PathVariable Long collaboratorId
    ) {
        taskService.assignCollaborator(taskId, boardId, collaboratorId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{taskId}/collaborators/{collaboratorId}")
    public ResponseEntity<Void> unassignCollaborator(
            @PathVariable Long boardId,
            @PathVariable String taskId,
            @PathVariable Long collaboratorId
    ) {
        taskService.unassignCollaborator(taskId, boardId, collaboratorId);
        return ResponseEntity.noContent().build();
    }
}
