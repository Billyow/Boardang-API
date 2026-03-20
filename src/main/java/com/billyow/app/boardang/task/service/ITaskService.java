package com.billyow.app.boardang.task.service;

import com.billyow.app.boardang.task.DTO.CreateTaskRequest;
import com.billyow.app.boardang.task.DTO.MoveTaskRequest;
import com.billyow.app.boardang.task.DTO.TaskResponse;
import com.billyow.app.boardang.task.DTO.UpdateTaskRequest;

import java.util.List;

public interface ITaskService {
    void createTask(CreateTaskRequest request);
    void deleteByTaskId(String taskId, Long boardId);
    void updateTask(UpdateTaskRequest request);
    List<TaskResponse> getTasksByCurrentUser();
    void moveTaskToColumn(MoveTaskRequest request);
    void assignCollaborator(String taskId, Long boardId, Long collaboratorId);
    void unassignCollaborator(String taskId, Long boardId, Long collaboratorId);
}
