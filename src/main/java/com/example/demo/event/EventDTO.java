package com.example.demo.event;

import com.example.demo.user.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class EventDTO {

    private String eventhash;
    private String content;
    private String statement;
    private boolean deletecheck;
    private String title;
    private String imgurl;
    private LocalDateTime date;
    private LocalDateTime enddate;
    private LocalDateTime fixdate;
    private UserDTO uid;
}