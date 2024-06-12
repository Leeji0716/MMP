package com.example.MMP.homeTraining;

import com.example.MMP.homeTraining.category.Category;
import com.example.MMP.siteuser.SiteUser;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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

    private LocalDateTime createDate;

    @ManyToMany
    private List<SiteUser> saver;

    @ManyToOne
    @JsonBackReference
    private Category category;

}
