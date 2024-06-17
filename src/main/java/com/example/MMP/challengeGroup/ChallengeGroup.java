package com.example.MMP.challengeGroup;

import com.example.MMP.challengeGroup.GroupTag.GroupTag;
import com.example.MMP.siteuser.SiteUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class ChallengeGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String goal;

    @ManyToOne
    @JoinColumn(name = "leader_id")
    @JsonIgnore
    private SiteUser leader;

    @ManyToMany
    @JoinTable(
            name = "group_user",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<SiteUser> members = new HashSet<>();

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupTag> groupTagList = new ArrayList<> ();

}

