package com.example.MMP.Comment;

import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.wod.Wod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @ManyToOne
    private Wod wod;
//
//    @ManyToOne
//    private SiteUser author;
//
//    @ManyToMany
//    Set<SiteUser> likeList;

//    @Column(nullable = false)
//    private Long likeCount; // 투표한 사용자 수를 나타내는 필드
}
