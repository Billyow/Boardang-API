package com.billyow.app.boardang.task.assembler;

import com.billyow.app.boardang.task.model.Task;
import com.billyow.app.boardang.task.DTO.TaskResponse;
import com.billyow.app.boardang.user.DTO.SimpleUserDTO;
import com.billyow.app.boardang.user.model.User;
import com.billyow.app.boardang.user.repository.IUserRepository;
import com.billyow.app.boardang.user.mapper.UserMapper;
import com.billyow.app.boardang.task.mapper.TaskMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class TaskResponseAssembler {

    private final IUserRepository userRepository;
    private final UserMapper userMapper;
    private final TaskMapper taskMapper;


    public List<TaskResponse> convertTasksToResponse(List<Task> tasks) {

        if (tasks == null || tasks.isEmpty()) {
            return List.of();
        }

        // 1. Collect all user IDs appearing in tasks (owners + collaborators)
        Set<Long> usersInTasks = tasks.stream()
                .flatMap(task -> {
                    Stream<Long> collaboratorsIds =
                            task.getCollaboratorsIds() != null
                                    ? task.getCollaboratorsIds().stream()
                                    : Stream.empty();

                    return Stream.concat(
                            Stream.of(task.getOwnerId()),
                            collaboratorsIds
                    );
                })
                .collect(Collectors.toSet());

        // 2. Fetch all these users in a single query and build a lookup map
        var listOfUsers = userRepository.findAllById(usersInTasks);
        Map<Long, SimpleUserDTO> usersById = new HashMap<>();

        for (User user : listOfUsers) {
            usersById.put(
                    user.getId(),
                    userMapper.toSimpleUserDTOResponse(user)
            );
        }

        // 3. Build and return the final TaskResponse list
        return tasks.stream()
                .map(task -> {

                    // Owner
                    var owner = usersById.get(task.getOwnerId());
                    if (owner == null) {
                        throw new RuntimeException("Owner not found for task with ownerId: " + task.getOwnerId());
                    }

                    // Collaborators
                    Set<SimpleUserDTO> members;

                    if (task.getCollaboratorsIds() == null || task.getCollaboratorsIds().isEmpty()) {
                        members = Set.of();
                    } else {
                        members = task.getCollaboratorsIds().stream()
                                .map(usersById::get)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet());
                    }

                    return taskMapper.toResponse(task, owner, members);
                })
                .collect(Collectors.toList());
    }
}
