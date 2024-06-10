package com.example.MMP.wod;

import com.example.MMP.Comment.Comment;
import com.example.MMP.siteuser.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Wod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "site_user_id") // 외래 키 컬럼 지정
    private SiteUser writer;

    private String imagePath;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToMany
    private List<SiteUser> likeList;

    private Long likeCount = 0L;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "wod", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

}
