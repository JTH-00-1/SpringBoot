package com.example.demo.user.entity;

import jakarta.persistence.*;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

@Data
@Entity
@DynamicInsert
public class User {
    @Id
    @Column(name="UID")
    private String userid;

    private String nickname;

    private String password;

    @Column(unique = true)
    private String phone;

    @Column(unique = true)
    private String Email;

    private String role;

    private Boolean bancheck;
}
