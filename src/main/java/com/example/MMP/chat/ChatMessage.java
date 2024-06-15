package com.example.MMP.chat;

import com.example.MMP.siteuser.SiteUser;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @ManyToOne
    private SiteUser sender;

    private LocalDateTime sendTime;

    @ManyToOne
    private ChatRoom chatRoom;


    @Builder
    private ChatMessage(String message,SiteUser sender,LocalDateTime sendTime,ChatRoom chatRoom){
        this.message = message;
        this.sender = sender;
        this.sendTime = sendTime;
        this.chatRoom = chatRoom;
    }
}
