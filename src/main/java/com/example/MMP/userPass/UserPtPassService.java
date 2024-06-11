package com.example.MMP.userPass;

import com.example.MMP.siteuser.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPtPassService {
    private final UserPtPassRepository userPtPassRepository;
    public UserPtPass UserPtAdd(String name, String title, int count, int price, int passDays, SiteUser siteUser){
        UserPtPass userPtPass = new UserPtPass();
        userPtPass.setPassName(name);
        userPtPass.setPassTitle(title);
        userPtPass.setPassCount(count);
        userPtPass.setPassPrice(price);
        userPtPass.setPassStart(LocalDateTime.now());
        LocalDateTime Finish = LocalDateTime.now().plusDays(passDays);
        userPtPass.setPassFinish(Finish);
        userPtPass.setSiteUser(siteUser);
        return userPtPassRepository.save(userPtPass);
    }

    public List<UserPtPass> findBySiteUser(SiteUser siteUser){
        return userPtPassRepository.findBySiteUser(siteUser);
    }
}
