package com.billyow.app.boardang.task.repository;

import com.billyow.app.boardang.task.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITaskRepository extends MongoRepository<Task, Long> {
    Task getTaskById(Long taskId);
    List<Task> getTasksByBoardId(Long boardId);
    List<Task> getTasksByColumnId(Long columnId);
    List<Task> getTasksByCollaboratorsIdsContains(Long collaboratorId);
}
