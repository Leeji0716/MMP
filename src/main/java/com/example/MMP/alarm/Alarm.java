package com.example.MMP.alarm;

import com.example.MMP.chat.ChatRoom;
import com.example.MMP.siteuser.SiteUser;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    private SiteUser acceptUser;

    private String message;

    @ManyToOne
    @JsonBackReference
    private ChatRoom chatRoom;

    @CreatedDate
    private LocalDateTime createDate;

    private LocalDateTime sendTime;

    private String sort;
    @Builder
    Alarm(String sender,SiteUser acceptUser,String message,LocalDateTime sendTime,ChatRoom chatRoom,String sort){
        this.sender = sender;
        this.acceptUser = acceptUser;
        this.message = message;
        this.sendTime = sendTime;
        this.chatRoom = chatRoom;
        this.sort = sort;
    }
}
