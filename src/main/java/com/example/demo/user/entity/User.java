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
    @Column(name="Nick_Name")
    private String nickname;

    private String password;

    @Column(unique = true)
    private String phone;

    @Column(unique = true)
    private String Email;

    private String role;
    @Column(name="Ban_Check")
    private Boolean bancheck;
}
