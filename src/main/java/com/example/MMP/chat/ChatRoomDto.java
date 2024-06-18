package com.example.MMP.chat;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ChatRoomDto {

    private String you;

    private Long youId;

    private String lastMessage;

    private LocalDateTime sendDate;

    private int alarmCnt;
}
