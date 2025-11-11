package com.billyow.app.boardang.task.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tasks")
public class Task {
    @Id
    private Long id;
    private String title;
    private String description;
    private Integer priority;
    private Integer position;

    // relational id's
    private Long boardId;
    private Long columnId;
    private Long createdBy;

    //collaborators id
    private Set<Long> collaboratorsIds;

    private Date createdAt;
    private Date updatedAt;
}
