package com.billyow.app.boardang.task.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UpdateTaskRequest(
    String id,
    String title,
    String description,
    @Min(value = 0, message = "Priority cannot be negative")
    Integer priority
){}
