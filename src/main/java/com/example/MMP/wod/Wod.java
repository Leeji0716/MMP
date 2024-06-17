package com.example.MMP.wod;

import com.example.MMP.Comment.Comment;
import com.example.MMP.siteuser.SiteUser;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JoinColumn(name = "writer_id")
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
