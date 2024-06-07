package com.example.MMP.trainer;

import com.example.MMP.Comment.Comment;
import com.example.MMP.siteuser.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainerId;

    private String trainerName;

    private String introduce;

    private String imagePath;


}
