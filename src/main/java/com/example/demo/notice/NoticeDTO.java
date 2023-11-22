package com.example.demo.notice;

import com.example.demo.user.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NoticeDTO {

    private String noticehash;
    private String content;
    private String statement;
    private boolean deletecheck;
    private String title;
    private String imgurl;
    private LocalDateTime date;
    private LocalDateTime fixdate;
    private UserDTO uid;
}