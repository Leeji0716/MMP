package com.example.MMP.point;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
    Point findBySiteUserId(Long siteUserId);
}
