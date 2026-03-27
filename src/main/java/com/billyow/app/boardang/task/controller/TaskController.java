package com.billyow.app.boardang.task.controller;

import com.billyow.app.boardang.task.DTO.CreateTaskRequest;
import com.billyow.app.boardang.task.DTO.MoveTaskRequest;
import com.billyow.app.boardang.task.DTO.TaskResponse;
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

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable String taskId) {
        return ResponseEntity.ok(taskService.getTaskDetails(taskId));
    }
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @PathVariable Long boardId,
            @Valid @RequestBody CreateTaskRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(boardId, request));
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable String taskId,
            @RequestBody UpdateTaskRequest request
    ) {
        return ResponseEntity.ok(taskService.updateTask(taskId, request));
    }

    @PatchMapping("/{taskId}/move")
    public ResponseEntity<TaskResponse> moveTask(
            @PathVariable Long boardId,
            @PathVariable String taskId,
            @RequestBody MoveTaskRequest request
    ) {
        return ResponseEntity.ok(taskService.moveTaskToColumn(taskId, boardId, request));
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
    public ResponseEntity<TaskResponse> assignCollaborator(
            @PathVariable Long boardId,
            @PathVariable String taskId,
            @PathVariable Long collaboratorId
    ) {
        return ResponseEntity.ok(taskService.assignCollaborator(taskId, boardId, collaboratorId));
    }

    @DeleteMapping("/{taskId}/collaborators/{collaboratorId}")
    public ResponseEntity<TaskResponse> unassignCollaborator(
            @PathVariable Long boardId,
            @PathVariable String taskId,
            @PathVariable Long collaboratorId
    ) {
        return ResponseEntity.ok(taskService.unassignCollaborator(taskId, boardId, collaboratorId));
    }
}
