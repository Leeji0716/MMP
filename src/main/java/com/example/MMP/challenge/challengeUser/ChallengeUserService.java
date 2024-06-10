package com.example.MMP.challenge.challengeUser;

import com.example.MMP.attendance.AttendanceService;
import com.example.MMP.challenge.challengeActivity.ChallengeActivity;
import com.example.MMP.challenge.challengeActivity.ChallengeActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChallengeUserService {

    private final ChallengeUserRepository challengeUserRepository;
    private final AttendanceService attendanceService;
    private final ChallengeActivityRepository challengeActivityRepository;


    public void updateAchievementRate(Long challengeUserId) {
        ChallengeUser challengeUser = challengeUserRepository.findById(challengeUserId)
                .orElseThrow(() -> new RuntimeException("챌린지 유저를 찾을 수 없습니다"));

        Long siteUserId = challengeUser.getSiteUser().getId();
        LocalDate startDate = challengeUser.getStartDate().toLocalDate();
        LocalDate endDate = challengeUser.getEndDate().toLocalDate();

        double attendanceRate = attendanceService.calculateAttendanceRate(siteUserId, startDate, endDate);
        challengeUser.setAchievementRate(attendanceRate);

        challengeUserRepository.save(challengeUser);
    }

    private double calculateActivityRate(List<ChallengeActivity> activities) {
        // 활동 데이터를 기반으로 달성률 계산 로직 구현
        int totalActivities = activities.size();
        if (totalActivities == 0) {
            return 0;
        }

        int successfulActivities = (int) activities.stream().filter(a -> a.getDuration() > 0 && a.getWeight() > 0).count();
        return ((double) successfulActivities / totalActivities) * 100;
    }

//    public double calculateAchievementRate(challengeUser challengeUser) {
//        // 예시: 성공 여부와 활동 데이터를 기반으로 달성률 계산
//        if (challengeUser.isSuccess ()) {
//            return 100.0;
//        } else {
//            if(challengeUser.)
//            // 실제 계산 로직은 요구사항에 따라 구현
    // 도원아 이 부분은 나중에 몸무게, 출석률 등을 계산할 때 로직을 넣으면 되는데 그거는 미래의 네가 로직을 짜야해
    // 예를 들어 출석이 30일이면 10%센트면 3일겠지? 이런식으로 10, 20, 30, 40, 50, 60, 70, 80, 90까지 넣어야 하는데
    // 10은 너무 많나? 네가 하고 싶은대로 해 (25 가 나을 거  같아)
//            double rate = ...;
//            return rate;
//        }
//    }



}
