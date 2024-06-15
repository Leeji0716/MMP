package com.example.MMP.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    public void save(ChatMessage chatMessage){
        chatMessageRepository.save(chatMessage);
    }
}
