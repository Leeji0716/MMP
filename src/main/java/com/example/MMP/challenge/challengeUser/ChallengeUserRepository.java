package com.example.MMP.challenge.challengeUser;

import com.example.MMP.challenge.challenge.Challenge;
import com.example.MMP.challenge.challenge.ChallengeRepository;
import com.example.MMP.siteuser.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengeUserRepository extends JpaRepository<ChallengeUser,Long> {

    Optional<ChallengeUser> findByChallengeAndSiteUser(Challenge challenge, SiteUser siteUser);
    List<ChallengeUser> findBySiteUser(SiteUser siteUser);
    List<ChallengeUser> findByChallenge(Challenge challenge);

}
