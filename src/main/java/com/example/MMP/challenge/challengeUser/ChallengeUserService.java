package com.example.MMP.challenge.challengeUser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChallengeUserService {

    private final ChallengeUserRepository challengeUserRepository;

    public void achievementRate(Long challengeUserId, double rate) {
        challengeUser challengeUser = challengeUserRepository.findById(challengeUserId).orElseThrow(() -> new RuntimeException("챌린지 유저를 찾을 수 없습니다"));
        // 나중에 100을 rate로 변경해야 함(지금은 그냥 테스트로 강제로 넣어둠)
        challengeUser.setAchievementRate(100);
        challengeUserRepository.save(challengeUser);
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
