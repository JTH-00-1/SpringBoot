package com.example.demo.user.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserUpdateForm {
    @NotEmpty(message = "사용자이름은 필수항목입니다.")
    private String username;

    @NotEmpty(message = "아이디는 필수항목입니다.")
    private String userid;

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email
    private String email;

    @NotEmpty(message="핸드폰번호는 필수항목입니다.")
    private String phone;

}
