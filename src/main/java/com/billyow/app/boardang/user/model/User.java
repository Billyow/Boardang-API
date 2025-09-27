package com.billyow.app.boardang.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class User {
    @Id
    private Long id;
    @Column(unique=true, nullable=false)
    private String email;
    @Column(nullable=false)
    private String password;
    @Column(nullable=false)
    private Boolean isActive;
    @Column(nullable=false,updatable = false)
    private Date createdAt;
    private Date updatedAt;
    @PrePersist
    private void setCreationDate(){
        this.createdAt = new Date();
    }
}
