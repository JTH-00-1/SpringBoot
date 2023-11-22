package com.example.demo.user.entity;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class UserCreateForm {

    @NotEmpty(message = "사용자이름은 필수항목입니다.")
    private String username;

    @NotEmpty(message = "사용자ID는 필수항목입니다.")
    private String userid;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String password2;

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email
    private String email;
    
    @NotEmpty(message="핸드폰번호는 필수항목입니다.")
    private String phone;
}
