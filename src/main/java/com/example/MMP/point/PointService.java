package com.example.MMP.point;

import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;
    private final SiteUserRepository siteUserRepository;

    @Transactional
    public void addPoints(Long userId, int pointsToAdd) {
        Point point = pointRepository.findBySiteUserId(userId);
        if (point == null) {
            SiteUser user = siteUserRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));
            point = new Point();
            point.setSiteUser(user);
            pointRepository.save(point);
        }
        point.addPoints(pointsToAdd);
        pointRepository.save(point);
    }

}
