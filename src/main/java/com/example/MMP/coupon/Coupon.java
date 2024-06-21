package com.example.MMP.coupon;

import com.example.MMP.siteuser.SiteUser;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String name;

    private int point;

    private int discount;

    @ManyToMany(mappedBy = "couponList")
    @JsonManagedReference
    private List<SiteUser> users;
}
