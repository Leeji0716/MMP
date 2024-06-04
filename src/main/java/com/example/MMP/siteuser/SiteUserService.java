package com.example.MMP.siteuser;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SiteUserService {
private final SiteUserRepository siteUserRepository;
private final PasswordEncoder passwordEncoder;
private int adminCount = 1;
public void adminSignup(String name,String number,String gender,String birthDay,String email){
    SiteUser siteUser = new SiteUser();
    siteUser.setUserId("admin"+adminCount);
    siteUser.setPassword(passwordEncoder.encode(number));
    siteUser.setName(name);
    siteUser.setNumber(number);
    siteUser.setBirthDate(birthDay);
    siteUser.setGender(gender);
    siteUser.setEmail(email);
    siteUser.setUserRole("admin");
    siteUserRepository.save(siteUser);
    adminCount++;
}

}


