package com.example.MMP.ptGroup;

import com.example.MMP.siteuser.SiteUser;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @OneToOne(mappedBy = "ptGroupTrainer")
    @JsonBackReference
    private SiteUser trainer;


    @ManyToMany
    @JoinTable(name = "ptgroup_siteuser",
            joinColumns = @JoinColumn(name = "ptgroup_id"),
            inverseJoinColumns = @JoinColumn(name = "siteuser_id"))
    @JsonIgnore
    private List<SiteUser> members = new ArrayList<>();
}
