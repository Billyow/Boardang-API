package com.billyow.app.boardang.task.service;

import com.billyow.app.boardang.task.DTO.CreateTaskRequest;
import com.billyow.app.boardang.task.DTO.MoveTaskRequest;
import com.billyow.app.boardang.task.DTO.TaskResponse;

import java.util.List;

public interface ITaskService {
    void createTask(CreateTaskRequest request);
    void deleteByBoardId(Long boardId);
    void deleteByBoardColumnId(Long columnId);
    void deleteByTaskId(String taskId, Long boardId);
    List<TaskResponse> getTasksByBoardColumnId(Long columnId);
    void moveTaskToColumn(MoveTaskRequest request);
}
