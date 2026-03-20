package com.billyow.app.boardang.task.service;

import com.billyow.app.boardang.task.DTO.CreateTaskRequest;
import com.billyow.app.boardang.task.DTO.MoveTaskRequest;
import com.billyow.app.boardang.task.DTO.TaskResponse;
import com.billyow.app.boardang.task.DTO.UpdateTaskRequest;

import java.util.List;

public interface ITaskService {
    void createTask(Long boardId, CreateTaskRequest request);
    void deleteByTaskId(String taskId, Long boardId);
    void updateTask(String taskId, UpdateTaskRequest request);
    List<TaskResponse> getTasksByCurrentUser();
    void moveTaskToColumn(String taskId, Long boardId, MoveTaskRequest request);
    void assignCollaborator(String taskId, Long boardId, Long collaboratorId);
    void unassignCollaborator(String taskId, Long boardId, Long collaboratorId);
}
