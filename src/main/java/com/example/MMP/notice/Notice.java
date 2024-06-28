package com.example.MMP.notice;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private int hit;

    private LocalDateTime notificationDate;

    @Column(columnDefinition = "integer default 0", nullable = false)
    public void increaseHit(){
        this.hit++;
    }

    private LocalDateTime modifyDate;
}
