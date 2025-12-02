package com.billyow.app.boardang.task.controller;

import com.billyow.app.boardang.task.DTO.CreateTaskRequest;
import com.billyow.app.boardang.task.DTO.MoveTaskRequest;
import com.billyow.app.boardang.task.DTO.TaskResponse;
import com.billyow.app.boardang.task.service.ITaskService;
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
    public ResponseEntity<TaskResponse> createTask(
            @PathVariable Long boardId,
            @RequestBody CreateTaskRequest request
    ) {
        CreateTaskRequest adjustedRequest = new CreateTaskRequest(
                request.title(),
                request.description(),
                request.priority(),
                request.columnId(),
                boardId
        );
        taskService.createTask(adjustedRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{taskId}/move")
    public ResponseEntity<Void> moveTask(
            @PathVariable Long boardId,
            @PathVariable String taskId,
            @RequestParam Long targetColumnId
            ) {
        MoveTaskRequest request = new MoveTaskRequest(
                taskId,
                targetColumnId,
                boardId
        );
        taskService.moveTaskToColumn(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{taskId}/delete")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long boardId,
            @PathVariable String taskId
    ){
        taskService.deleteByTaskId(taskId,boardId);
        return ResponseEntity.noContent().build();
    }


}