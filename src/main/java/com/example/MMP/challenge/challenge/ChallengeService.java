package com.example.MMP.challenge.challenge;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    public Challenge create(String name, String description, LocalDateTime startDate, LocalDateTime endDate, int requiredPoint){

        Challenge challenge = new Challenge ();

        challenge.setName (name);
        challenge.setDescription (description);
        challenge.setOpenDate (startDate);
        challenge.setCloseDate (endDate);
        challenge.setRequiredPoint (requiredPoint);

        return challengeRepository.save (challenge);
    }
}
