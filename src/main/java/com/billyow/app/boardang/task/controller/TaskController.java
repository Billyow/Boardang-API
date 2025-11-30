package com.billyow.app.boardang.task.controller;

import com.billyow.app.boardang.task.DTO.CreateTaskRequest;
import com.billyow.app.boardang.task.DTO.TaskResponse;
import com.billyow.app.boardang.task.service.ITaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/board/{boardId}/tasks")
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
}