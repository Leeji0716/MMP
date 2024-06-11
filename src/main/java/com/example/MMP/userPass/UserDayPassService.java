package com.example.MMP.userPass;

import com.example.MMP.siteuser.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDayPassService {
    private final UserDayPassRepository userDayPassRepository;

    public UserDayPass UserDayadd(String name, String title, int price, int passDays, SiteUser siteUser){
        UserDayPass userDayPass = new UserDayPass();
        userDayPass.setPassName(name);
        userDayPass.setPassTitle(title);
        userDayPass.setPassPrice(price);
        userDayPass.setPassStart(LocalDateTime.now());
        userDayPass.setPassFinish(userDayPass.getPassStart().plusDays(passDays));
        userDayPass.setSiteUser(siteUser);
        return userDayPassRepository.save(userDayPass);
    }

    public List<UserDayPass> findBySiteUser(SiteUser siteUser){

        return userDayPassRepository.findBySiteUser(siteUser);
    }
}
