package com.example.MMP.userPass;

import com.example.MMP.siteuser.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        userPtPass.setPassStart(LocalDate.now());
        LocalDate Finish = LocalDate.now().plusDays(passDays);
        userPtPass.setPassFinish(Finish);
        userPtPass.setSiteUser(siteUser);
        return userPtPassRepository.save(userPtPass);
    }

    public List<UserPtPass> findBySiteUser(SiteUser siteUser){
        return userPtPassRepository.findBySiteUser(siteUser);
    }

    public UserPtPass findByPassName(String passName) {
        return userPtPassRepository.findByPassName(passName);
    }

    public UserPtPass findByPassId(Long id) {
        return userPtPassRepository.findById(id).orElse(null);
    }

    public void save(UserPtPass userPtPass) {
        userPtPassRepository.save(userPtPass);
    }

    public List<UserPtPass> findfinshTime(SiteUser siteUser){
        return userPtPassRepository.findAllBySiteUserOrderByPassFinishAsc(siteUser);
    }

    public void delete(UserPtPass userPtPass) {
        userPtPassRepository.delete(userPtPass);
    }
}
