package com.example.MMP.challenge.challengeUser;

import com.example.MMP.challenge.challenge.Challenge;
import com.example.MMP.siteuser.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengeUserRepository extends JpaRepository<challengeUser,Long> {

    Optional<challengeUser> findByChallengeAndSiteUser(Challenge challenge, SiteUser siteUser);
    List<challengeUser> findBySiteUser(SiteUser siteUser);
}
