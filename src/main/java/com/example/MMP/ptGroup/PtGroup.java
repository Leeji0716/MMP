package com.example.MMP.ptGroup;

import com.example.MMP.siteuser.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
public class PtGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private SiteUser trainer;

    @OneToMany(mappedBy = "ptGroupUser")
    private List<SiteUser> members = new ArrayList<>();
}
