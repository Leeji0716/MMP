package com.example.MMP.chat;

import com.example.MMP.alarm.Alarm;
import com.example.MMP.alarm.AlarmService;
import com.example.MMP.challengeGroup.ChallengeGroup;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final AlarmService alarmService;
    private final SiteUserService siteUserService;

    public ChatRoom findChatroom(SiteUser siteUser, SiteUser siteUser1) {
        Optional<ChatRoom> _chatRoom = chatRoomRepository.findChatroom(siteUser, siteUser1);
        if (_chatRoom.isEmpty()) {
            return create(siteUser, siteUser1);
        } else {
            ChatRoom chatRoom = _chatRoom.get();
            return chatRoom;
        }
    }

    public ChatRoom create(SiteUser siteUser, SiteUser siteUser1) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.getUserList().add(siteUser);
        chatRoom.getUserList().add(siteUser1);
        chatRoomRepository.save(chatRoom);

        return chatRoom;
    }

    public ChatRoom findById(Long id) {
        return chatRoomRepository.findById(id).orElseThrow();
    }


    public void save(ChatRoom chatRoom) {
        chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public List<ChatRoomDto> findChat(SiteUser siteUser) {
        List<ChatRoom> chatRoomList = siteUser.getChatRoomList();
        List<ChatRoomDto> chatRoomDtoList = new ArrayList<>();

        for (ChatRoom chatRoom : chatRoomList) {
            int cnt = 0;
            int groupCnt =0;
            ChatRoomDto chatRoomDto = new ChatRoomDto();

            ChatMessage chatMessage = chatRoom.getChatMessageList().get(chatRoom.getChatMessageList().size() - 1);
            chatRoomDto.setLastMessage(chatMessage.getMessage());
            chatRoomDto.setSendDate(chatMessage.getSendTime());

            if(chatMessage.getSort().equals("many")){

                List<ChallengeGroup> challengeGroupList = new ArrayList<>(siteUser.getChallengeGroups());
                 chatRoomDto.setYou(challengeGroupList.get(groupCnt).getName());
                chatRoomDto.setYouId(challengeGroupList.get(groupCnt).getId());
            }else{
                if(chatRoom.getUserList().size() > 1) {
                    for (SiteUser siteUser1 : chatRoom.getUserList()) {
                        if (siteUser1.getId() != siteUser.getId()) {
                            chatRoomDto.setYou(siteUser1.getName());
                            chatRoomDto.setYouId(siteUser1.getId());
                            break;
                        }
                    }
                }else{
                    chatRoomDto.setYouId(null);
                    chatRoomDto.setYou(null);
                }
            }
            for (Alarm alarm : chatRoom.getAlarmList()) {
                if (alarm.getAcceptUser().getId() == siteUser.getId()) {
                    cnt++;
                }
            }
            chatRoomDto.setAlarmCnt(cnt);
            if(chatRoomDto.getYou() != null && chatRoomDto.getYouId() != null)
                chatRoomDtoList.add(chatRoomDto);
        }
        return chatRoomDtoList;
    }

    public void deleteAlarm(SiteUser siteUser, ChatRoom chatRoom) {
        for (Alarm alarm : chatRoom.getAlarmList()) {
            for (Alarm alarm1 : siteUser.getAlarmList()) {
                if (alarm.getId() == alarm1.getId()) {
                    alarmService.delete(alarm);
                }
            }
        }
    }

    @Transactional
    public ChatRoomDto findAlarm(Long id, Long meId) {
        SiteUser siteUser = siteUserService.findById(id);
        SiteUser me = siteUserService.findById(meId);
        List<ChatRoom> chatRoomList = siteUser.getChatRoomList();
        ChatRoomDto chatRoomDto = new ChatRoomDto();
        for (ChatRoom chatRoom : chatRoomList) {
            int cnt = 0;
            ChatMessage chatMessage = chatRoom.getChatMessageList().get(chatRoom.getChatMessageList().size() - 1);
            chatRoomDto.setLastMessage(chatMessage.getMessage());
            chatRoomDto.setSendDate(chatMessage.getSendTime());
            for (SiteUser siteUser1 : chatRoom.getUserList()) {
                if (siteUser1.getId() != siteUser.getId()) {
                    chatRoomDto.setYou(siteUser1.getName());
                    chatRoomDto.setYouId(siteUser1.getId());
                    break;
                }
            }
            for (Alarm alarm : chatRoom.getAlarmList()) {
                if (alarm.getAcceptUser().getId().equals(siteUser.getId()) && alarm.getSender().equals(me.getNumber())) {
                    cnt++;
                    chatRoomDto.setAlarmCnt(cnt);
                }
            }
        }
        return chatRoomDto;
    }
}
