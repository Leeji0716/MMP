package com.example.MMP.point;

import com.example.MMP.siteuser.SiteUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int points;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_user_id")
    @JsonIgnore
    private SiteUser siteUser;

    // 기본 생성자
    public Point() {
        this.points = 0; // 기본 포인트 초기값 설정
    }

    // 포인트 추가 메서드
    public void addPoints(int pointsToAdd) {
        this.points += pointsToAdd;
    }
}
