package com.example.MMP.alarm;

import com.example.MMP.chat.ChatRoom;
import com.example.MMP.siteuser.SiteUser;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.security.Principal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;
    @ManyToOne
    private SiteUser acceptUser;

    private String message;

    @ManyToOne
    private ChatRoom chatRoom;

    @CreatedDate
    private LocalDateTime createDate;

    private LocalDateTime sendTime;
    @Builder
    Alarm(String sender,SiteUser acceptUser,String message,LocalDateTime sendTime,ChatRoom chatRoom){
        this.sender = sender;
        this.acceptUser = acceptUser;
        this.message = message;
        this.sendTime = sendTime;
        this.chatRoom = chatRoom;
    }
}
