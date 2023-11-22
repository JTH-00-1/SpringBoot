package com.example.demo.user.dto;
import lombok.Data;

@Data
public class UserDTO {
    private String userid;
    private String nickname;
    private String userpw;
    private String userphone;
    private String useremail;
    private String role;
    private Boolean Bancheck;
}
