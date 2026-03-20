package com.billyow.app.boardang.task.service;

import com.billyow.app.boardang.task.DTO.CreateTaskRequest;
import com.billyow.app.boardang.task.DTO.MoveTaskRequest;
import com.billyow.app.boardang.task.DTO.TaskResponse;
import com.billyow.app.boardang.task.DTO.UpdateTaskRequest;

import java.util.List;

public interface ITaskService {
    TaskResponse createTask(Long boardId, CreateTaskRequest request);
    void deleteByTaskId(String taskId, Long boardId);
    TaskResponse updateTask(String taskId, UpdateTaskRequest request);
    List<TaskResponse> getTasksByCurrentUser();
    TaskResponse moveTaskToColumn(String taskId, Long boardId, MoveTaskRequest request);
    TaskResponse assignCollaborator(String taskId, Long boardId, Long collaboratorId);
    TaskResponse unassignCollaborator(String taskId, Long boardId, Long collaboratorId);
}
