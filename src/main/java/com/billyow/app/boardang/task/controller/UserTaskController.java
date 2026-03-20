package com.billyow.app.boardang.task.controller;

import com.billyow.app.boardang.task.DTO.TaskResponse;
import com.billyow.app.boardang.task.service.ITaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tasks")
public class UserTaskController {

    private final ITaskService taskService;

    @GetMapping("/me")
    public ResponseEntity<List<TaskResponse>> getMyTasks() {
        return ResponseEntity.ok(taskService.getTasksByCurrentUser());
    }
}
