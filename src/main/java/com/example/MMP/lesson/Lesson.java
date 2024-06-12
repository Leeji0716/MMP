package com.example.MMP.lesson;

import com.example.MMP.siteuser.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private SiteUser trainer;

    private String lessonName;

    private int headCount;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

//    @ManyToMany
//    private List<SiteUser> participants;
}
