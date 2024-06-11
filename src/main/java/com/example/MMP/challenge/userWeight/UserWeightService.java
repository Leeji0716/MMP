package com.example.MMP.challenge.userWeight;

import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserWeightService {
    private final UserWeightRepository userWeightRepository;
    private final SiteUserRepository siteUserRepository;

    public void recordInitialWeight(Long userId, double weight) {
        SiteUser siteUser = siteUserRepository.findById(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        UserWeight userWeight = new UserWeight();
        userWeight.setSiteUser(siteUser);
        userWeight.setWeight(weight);
        userWeight.setRecordedAt(LocalDateTime.now());
        userWeightRepository.save(userWeight);
    }

    public void recordWeight(Long userId, double weight) {
        SiteUser siteUser = siteUserRepository.findById(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        UserWeight userWeight = new UserWeight();
        userWeight.setSiteUser(siteUser);
        userWeight.setWeight(weight);
        userWeight.setRecordedAt(LocalDateTime.now());
        userWeightRepository.save(userWeight);
    }

    public double getInitialWeight(Long userId) {
        return userWeightRepository.findTopBySiteUserIdOrderByRecordedAtAsc(userId)
                .orElseThrow(() -> new RuntimeException("초기 몸무게를 찾을 수 없습니다."))
                .getWeight();
    }

    public List<UserWeight> getUserWeights(Long userId) {
        return userWeightRepository.findBySiteUserId(userId);
    }
}
