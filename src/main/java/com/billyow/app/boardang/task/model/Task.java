package com.billyow.app.boardang.task.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tasks")
public class Task {
    @MongoId
    private String id;
    private String title;
    private String description;
    private Integer priority;
    private Integer position;

    // relational id's
    @Indexed
    private Long boardId;
    @Indexed
    private Long columnId;
    @Indexed
    private Long ownerId;

    //collaborators id
    @Indexed
    private Set<Long> collaboratorsIds;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;

}
