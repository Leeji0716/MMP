package com.example.MMP.siteuser;

import com.example.MMP.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SiteUserService {
    private final SiteUserRepository siteUserRepository;
    private final PasswordEncoder passwordEncoder;


    public void adminSignup(String name, String number, String gender, String birthDay, String email) {
        SiteUser siteUser = new SiteUser();
        siteUser.setPassword(passwordEncoder.encode(number));
        siteUser.setName(name);
        siteUser.setNumber(number);
        siteUser.setBirthDate(birthDay);
        siteUser.setGender(gender);
        siteUser.setEmail(email);
        siteUser.setUserRole("admin");
        siteUserRepository.save(siteUser);
        siteUser.setUserId("admin" + siteUser.getId());
        siteUserRepository.save(siteUser);
    }

    public SiteUser userSignup(String name,String number,String gender, String birthDay, String email, String userRole){
        SiteUser siteUser = new SiteUser();
        siteUser.setUserId(number);
        siteUser.setPassword(passwordEncoder.encode(birthDay));
        siteUser.setName(name);
        siteUser.setNumber(number);
        siteUser.setBirthDate(birthDay);
        siteUser.setGender(gender);
        siteUser.setEmail(email);
        siteUser.setUserRole(userRole);
        return siteUserRepository.save(siteUser);
    }

    public SiteUser getUser(String name) {
        SiteUser siteUser = siteUserRepository.findByUserId(name).get();
        return siteUser;
    }

    public SiteUser getUserByUsername(String username) {
        Optional<SiteUser> siteUser = this.siteUserRepository.findByName (username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException ("사용자를 찾을 수 없습니다.");
        }
    }


    public void changePassword(Long userId, String currentPassword, String newPassword) throws Exception {
        SiteUser user = siteUserRepository.findById(userId).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new Exception("현재 비밀번호가 올바르지 않습니다.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        siteUserRepository.save(user);
    }


    public SiteUser findByUserName(String username){
        return siteUserRepository.findByUserId(username).orElseThrow();
    }

}


