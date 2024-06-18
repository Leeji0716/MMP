package com.example.MMP.websocket;

import com.example.MMP.alarm.Alarm;
import com.example.MMP.alarm.AlarmDto;
import com.example.MMP.alarm.AlarmService;
import com.example.MMP.chat.*;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebsocketController {
    private final SiteUserService siteUserService;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final AlarmService alarmService;

    @MessageMapping("/talk/{id}")
    @SendTo("/sub/talk/{id}")
    public ChatMessageDto chat(@DestinationVariable("id") Long id, ChatMessageDto chatMessageDto) {
        SiteUser siteUser = siteUserService.findByNumber(chatMessageDto.getSender());
        ChatRoom chatRoom = chatRoomService.findById(id);
        ChatMessage chatMessage = ChatMessage.builder().message(chatMessageDto.getMessage()).sender(siteUser).sendTime(chatMessageDto.getSendTime()).chatRoom(chatRoom).build();
        chatMessageService.save(chatMessage);

        return chatMessageDto;
    }

    @MessageMapping("/alarm/{number}")
    @SendTo("/sub/alarm/{number}")
    public AlarmDto alarm(@DestinationVariable("number") String number, AlarmDto alarmDto) {
        SiteUser siteUser = siteUserService.findByNumber(alarmDto.getAcceptUser());
        ChatRoom chatRoom = chatRoomService.findById(alarmDto.getChatroomId());
        Alarm alarm = Alarm.builder().sender(alarmDto.getSender()).acceptUser(siteUser).message(alarmDto.getMessage()).sendTime(alarmDto.getSendTime()).chatRoom(chatRoom).build();
        alarmService.save(alarm);
        ChatRoomDto chatRoomDto = chatRoomService.findAlarm(siteUser.getId());
        SiteUser me = siteUserService.findByNumber(alarmDto.getSender());
        alarmDto.setSender(me.getName());
        alarmDto.setAlarmCnt(chatRoomDto.getAlarmCnt() + 1);

        return alarmDto;
    }
}
