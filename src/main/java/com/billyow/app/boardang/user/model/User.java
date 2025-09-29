package com.billyow.app.boardang.user.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
@Entity
public class User implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
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
    private void prePersist(){
        this.createdAt = new Date();
        this.isActive = true;
    }

}
