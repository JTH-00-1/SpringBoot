package com.example.demo.user.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Setter
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
