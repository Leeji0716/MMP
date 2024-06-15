package com.example.MMP.chat;

import com.example.MMP.siteuser.SiteUser;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private SiteUser trainer;
    @ManyToOne
    private SiteUser user;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatMessage> chatMessageList = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createDate;

    @Builder
   private ChatRoom(SiteUser trainer,SiteUser user){
        this.trainer = trainer;
        this.user = user;
    }
}
