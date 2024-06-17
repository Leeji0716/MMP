package com.example.MMP.alarm;

import com.example.MMP.siteuser.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.security.Principal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;
    @ManyToOne
    private SiteUser acceptUser;

    @CreatedDate
    private LocalDateTime createDate;

    private LocalDateTime sendTime;
}
