package com.billyow.app.boardang.task.service;

import com.billyow.app.boardang.task.DTO.CreateTaskRequest;
import com.billyow.app.boardang.task.model.Task;

public interface ITaskService {
    Task createTask(CreateTaskRequest task);
    void deleteByBoardId(Long boardId);
    void deleteByBoardColumnId(Long columnId);
}
