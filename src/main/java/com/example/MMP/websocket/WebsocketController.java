package com.example.MMP.websocket;

import com.example.MMP.chat.*;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebsocketController {
    private final SiteUserService siteUserService;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/talk/{id}")
    @SendTo("/sub/talk/{id}")
    public ChatMessageDto chat(@DestinationVariable("id")Long id,ChatMessageDto chatMessageDto){
        SiteUser siteUser = siteUserService.findByNumber(chatMessageDto.getSender());
        ChatRoom chatRoom = chatRoomService.findById(id);
        ChatMessage chatMessage = ChatMessage.builder().message(chatMessageDto.getMessage()).sender(siteUser).sendTime(chatMessageDto.getSendTime()).chatRoom(chatRoom).build();
        chatMessageService.save(chatMessage);

        return chatMessageDto;
    }
}
