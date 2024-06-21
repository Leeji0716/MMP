package com.example.MMP.chat;

import com.example.MMP.alarm.Alarm;
import com.example.MMP.siteuser.SiteUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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
@JsonIgnoreProperties({"chatMessageList", "alarmList"})
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "chatRoomList", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    List<SiteUser> userList = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom")
//    @JsonManagedReference
    @JsonIgnore
    private List<ChatMessage> chatMessageList = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "chatRoom")
//    @JsonManagedReference
    @JsonIgnore
    List<Alarm> alarmList = new ArrayList<>();

    private String sort;
}
