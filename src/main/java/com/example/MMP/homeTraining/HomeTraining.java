package com.example.MMP.homeTraining;

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

    //    @ManyToOne
//    private SiteUser writer;
    private String videoUrl;

    @Column(columnDefinition = "TEXT")
    private String content;

    //    @ManyToMany
//    private List<SiteUser> likeList;

//    private Long likeCount;

    private LocalDateTime createDate;

}
