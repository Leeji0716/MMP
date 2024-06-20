package com.example.MMP.chat;

import com.example.MMP.siteuser.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    public void save(ChatMessage chatMessage){
        chatMessageRepository.save(chatMessage);
    }

    public void firstChatMessage(SiteUser siteUser,ChatRoom chatRoom){
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(siteUser);
        chatMessage.setMessage(chatMessage.getSender().getName()+"의 PT그룹에 참여한걸 환영합니다...!");
        chatMessage.setSendTime(LocalDateTime.now());
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setSort("one");
        chatMessageRepository.save(chatMessage);
    }

    public void firstGroupChatMessage(SiteUser siteUser,ChatRoom chatRoom,String groupName){
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(siteUser);
        chatMessage.setMessage(groupName+"에 참여한걸 환영합니다...!");
        chatMessage.setSendTime(LocalDateTime.now());
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setSort("many");
        chatMessageRepository.save(chatMessage);
    }
}
