package com.example.MMP.challenge.challenge;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    public Challenge create(String name, String description, LocalDateTime startDate, LocalDateTime endDate, int requiredPoint, String type){

        // 입력 값이 널이 아니면 챌린지에 대해 생성을 해준다
        Challenge challenge = new Challenge ();

        challenge.setName (name);
        challenge.setDescription (description);
        challenge.setOpenDate (startDate);
        challenge.setCloseDate (endDate);
        challenge.setRequiredPoint (requiredPoint);
        challenge.setType (type);

        return challengeRepository.save (challenge);
    }

    public List<Challenge> getAllChallenges(){
        return challengeRepository.findAll ();
    }
}
