package com.example.MMP.challengeGroup.GroupTag;


import com.example.MMP.Tag.Tag;
import com.example.MMP.challengeGroup.ChallengeGroup;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class GroupTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private ChallengeGroup group;
}