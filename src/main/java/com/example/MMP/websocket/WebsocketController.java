package com.example.MMP.websocket;

import com.example.MMP.alarm.Alarm;
import com.example.MMP.alarm.AlarmDto;
import com.example.MMP.alarm.AlarmService;
import com.example.MMP.chat.*;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class WebsocketController {
    private final SiteUserService siteUserService;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final AlarmService alarmService;
    private final SimpUserRegistry simpUserRegistry;

    @MessageMapping("/talk/{id}")
    @SendTo("/sub/talk/{id}")
    public ChatMessageDto chat(@DestinationVariable("id") Long id, ChatMessageDto chatMessageDto) {
        SiteUser siteUser = siteUserService.findByNumber(chatMessageDto.getSender());
        ChatRoom chatRoom = chatRoomService.findById(id);
        ChatMessage chatMessage = ChatMessage.builder().message(chatMessageDto.getMessage()).sender(siteUser).sendTime(chatMessageDto.getSendTime()).chatRoom(chatRoom).sort(chatMessageDto.getSort()).build();
        chatMessageService.save(chatMessage);

        return chatMessageDto;
    }

    @MessageMapping("/alarm/{number}")
    @SendTo("/sub/alarm/{number}")
    public AlarmDto alarm(@DestinationVariable("number") String number, AlarmDto alarmDto) {
        SiteUser siteUser = siteUserService.findByNumber(alarmDto.getAcceptUser());
        ChatRoom chatRoom = chatRoomService.findById(alarmDto.getChatroomId());
        Alarm alarm = Alarm.builder().sender(alarmDto.getSender()).acceptUser(siteUser).message(alarmDto.getMessage()).sendTime(alarmDto.getSendTime()).chatRoom(chatRoom).sort(alarmDto.getSort()).build();
        alarmService.save(alarm);
        SiteUser me = siteUserService.findByNumber(alarmDto.getSender());
        ChatRoomDto chatRoomDto = chatRoomService.findAlarm(siteUser.getId(),me.getId());
        alarmDto.setSender(me.getName());
        alarmDto.setAlarmCnt(chatRoomDto.getAlarmCnt() -1 + 1);

        return alarmDto;
    }

    @MessageMapping("/groupAlarm/{number}")
    @SendTo("/sub/groupAlarm/{number}")
    public AlarmDto groupAlarm(@DestinationVariable("number") String number,AlarmDto alarmDto){
        SiteUser siteUser = siteUserService.findByNumber(alarmDto.getAcceptUser());
        ChatRoom chatRoom = chatRoomService.findById(alarmDto.getChatroomId());
        Alarm alarm = Alarm.builder().sender(alarmDto.getSender()).acceptUser(siteUser).message(alarmDto.getMessage()).sendTime(alarmDto.getSendTime()).chatRoom(chatRoom).sort(alarmDto.getSort()).build();
        alarmService.save(alarm);
        ChatRoomDto chatRoomDto = chatRoomService.findGroupAlarm(siteUser.getId(),alarmDto.getSender());
        alarmDto.setSender(alarmDto.getSender());
        alarmDto.setAlarmCnt(chatRoomDto.getAlarmCnt() -1 + 1);

        return alarmDto;
    }

    @MessageMapping("/user/join/{id}")
    @SendTo("/sub/user/join/{id}")
    public List<String> getUserJoin(@DestinationVariable("id")Long id,SessionDto sessionDto){
        Set<SimpUser> users = simpUserRegistry.getUsers();
        List<String> subscriberIds = new ArrayList<>();

        for (SimpUser user : users) {
            for (SimpSession session : user.getSessions()) {
                for (SimpSubscription subscription : session.getSubscriptions()) {
                    if (subscription.getDestination().equals("/sub/talk/"+id)) {
                        subscriberIds.add(user.getName());
                    }
                }
            }
        }

        if(sessionDto.getSort().equals("out")) {
            for (String name : subscriberIds) {
                if (name.equals(sessionDto.getName())) {
                    subscriberIds.remove(sessionDto.getName());
                    break;
                }
            }
            return subscriberIds;
        }
        return subscriberIds;
    }
}
