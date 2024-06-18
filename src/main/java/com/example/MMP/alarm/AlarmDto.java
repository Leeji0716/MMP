package com.example.MMP.alarm;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class AlarmDto {

    private String sender;

    private String acceptUser;

    private String message;

    private LocalDateTime sendTime;

    private Long chatroomId;

}
