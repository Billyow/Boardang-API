package com.billyow.app.boardang.task.mapper;

import com.billyow.app.boardang.task.DTO.CreateTaskRequest;
import com.billyow.app.boardang.task.DTO.TaskResponse;
import com.billyow.app.boardang.task.model.Task;
import com.billyow.app.boardang.user.DTO.SimpleUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "ownerId", source = "ownerId")
    @Mapping(target = "collaboratorsIds", source = "collaboratorsIds")
    Task toEntity(CreateTaskRequest request, Long ownerId, Set<Long> collaboratorsIds);

    @Mapping(target = "id", source = "task.id")
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "collaborators", source = "collaborators")
    TaskResponse toResponse(Task task, SimpleUserDTO owner, Set<SimpleUserDTO> collaborators);
}
