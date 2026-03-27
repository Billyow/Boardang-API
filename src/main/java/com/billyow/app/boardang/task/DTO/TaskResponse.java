package com.billyow.app.boardang.task.DTO;
import com.billyow.app.boardang.user.DTO.SimpleUserDTO;
import java.util.Set;

public record TaskResponse(String id,
                           String title,
                           String description,
                           Integer priority,
                           SimpleUserDTO createdBy,
                           Set<SimpleUserDTO> collaborators){
}
