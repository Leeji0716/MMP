package com.example.MMP.homeTraining;

import com.example.MMP.siteuser.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class HomeTraining {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private SiteUser writer;

    private String videoUrl;

    private String thumbnailUrl;

    @Column(columnDefinition = "TEXT")
    private String content;

    //    @ManyToMany
//    private List<SiteUser> likeList;

//    private Long likeCount;

    private LocalDateTime createDate;

}
