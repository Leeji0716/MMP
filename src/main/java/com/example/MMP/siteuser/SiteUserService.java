package com.example.MMP.siteuser;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public void userSignup(String name,String number,String gender, String birthDay, String email, String userRole){
        SiteUser siteUser = new SiteUser();
        siteUser.setUserId(number);
        siteUser.setPassword(passwordEncoder.encode(birthDay));
        siteUser.setName(name);
        siteUser.setNumber(number);
        siteUser.setBirthDate(birthDay);
        siteUser.setGender(gender);
        siteUser.setEmail(email);
        siteUser.setUserRole(userRole);
        siteUserRepository.save(siteUser);
    }
}


