package com.billyow.app.boardang.user.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import com.billyow.app.boardang.user.model.Role;

@Data
@Entity
public class User implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "User name cannot be blank")
    private String name;
    @Column(unique=true, nullable=false)
    private String email;
    @Column(nullable=false)
    private String password;
    @Column(nullable=false)
    private Boolean isActive;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @Column(nullable=false,updatable = false)
    private Date createdAt;
    private Date updatedAt;
    @PrePersist
    private void prePersist(){
        this.createdAt = new Date();
        this.isActive = true;
        this.role = Role.USER;
    }

}
