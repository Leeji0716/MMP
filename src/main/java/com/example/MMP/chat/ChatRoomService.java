package com.example.MMP.chat;

import com.example.MMP.siteuser.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom findChatroom(SiteUser siteUser, SiteUser siteUser1) {
        Optional<ChatRoom> _chatRoom = chatRoomRepository.findChatroom(siteUser,siteUser1);
        if(_chatRoom.isEmpty()){
            return create(siteUser,siteUser1);
        }else{
            ChatRoom chatRoom = _chatRoom.get();
            return chatRoom;
        }
    }

    public ChatRoom create(SiteUser siteUser,SiteUser siteUser1){
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
}
