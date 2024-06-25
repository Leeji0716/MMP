package com.example.MMP.siteuser;

import com.example.MMP.DataNotFoundException;

import com.example.MMP.lesson.Lesson;

import com.example.MMP.mail.MailService;
import com.example.MMP.point.Point;
import com.example.MMP.siteuser.salary.SalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SiteUserService {
    private final SiteUserRepository siteUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final SalaryService salaryService;

    @Transactional
    public void resetPassword(String userId, String email) throws Exception {
        SiteUser user = siteUserRepository.findByUserIdAndEmail (userId, email)
                .orElseThrow (() -> new Exception ("등록되지 않은 아이디 또는 이메일입니다."));
        String tempPassword = UUID.randomUUID ().toString ().substring (0, 8);
        user.setPassword (passwordEncoder.encode (tempPassword));
        siteUserRepository.save (user);
        mailService.mailSend (user.getEmail (), "비밀번호 재설정", "임시 비밀번호: " + tempPassword);
    }


    public void adminSignup(String name, String number, String gender, String birthDay, String email) {
        SiteUser siteUser = new SiteUser ();
        siteUser.setPassword (passwordEncoder.encode (number));
        siteUser.setName (name);
        siteUser.setNumber (number);
        siteUser.setBirthDate (birthDay);
        siteUser.setGender (gender);
        siteUser.setEmail (email);
        siteUser.setUserRole ("admin");
        siteUser.setCreateDate (LocalDate.now ());
        // Point 설정
        Point point = new Point ();
        point.setSiteUser (siteUser); // Point와 SiteUser 연결
        siteUser.setPoint (point); // SiteUser에 Point 설정

        siteUserRepository.save (siteUser);
        siteUser.setUserId ("admin" + siteUser.getId ());
        siteUserRepository.save (siteUser);
    }

    public SiteUser userSignup(String name, String number, String gender, String birthDay, String email, String userRole, int salary, Long referrerId) {
        SiteUser siteUser = new SiteUser ();
        siteUser.setUserId (number);
        siteUser.setPassword (passwordEncoder.encode (birthDay));
        siteUser.setName (name);
        siteUser.setNumber (number);
        siteUser.setBirthDate (birthDay);
        siteUser.setGender (gender);
        siteUser.setEmail (email);
        siteUser.setUserRole (userRole);
        siteUser.setCreateDate (LocalDate.now ());

        if ("trainer".equals (userRole)) {
            siteUser.setSalary (salary * 10000);
        }

        // 추천인 설정
        if (referrerId != null && !referrerId.equals (0) && "user".equals (userRole)) {
            Optional<SiteUser> optReferrer = siteUserRepository.findById (referrerId);
            if (optReferrer.isPresent ()) {
                SiteUser referrer = optReferrer.get ();
                siteUser.setReferrer (referrer);
                referrer.getReferrals ().add (siteUser);
            }
        }

        // Point 설정
        Point point = new Point ();
        point.setSiteUser (siteUser); // Point와 SiteUser 연결
        siteUser.setPoint (point); // SiteUser에 Point 설정

        return siteUserRepository.save (siteUser);
    }

    public SiteUser getUser(String name) {
        SiteUser siteUser = siteUserRepository.findByUserId (name).get ();
        return siteUser;
    }

    public void changePassword(Long userId, String currentPassword, String newPassword) throws Exception {
        SiteUser user = siteUserRepository.findById (userId).orElseThrow (() -> new Exception ("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches (currentPassword, user.getPassword ())) {
            throw new Exception ("현재 비밀번호가 올바르지 않습니다.");
        }

        user.setPassword (passwordEncoder.encode (newPassword));
        siteUserRepository.save (user);
    }


    public SiteUser findByUserName(String username) {
        return siteUserRepository.findByUserId (username).orElseThrow ();
    }

    public SiteUser findById(Long id) {
        return siteUserRepository.findById (id).orElseThrow ();
    }

    public void save(SiteUser member) {
        siteUserRepository.save (member);
    }

    public SiteUser findByNumber(String number) {
        Optional<SiteUser> optionalSiteUser = siteUserRepository.findByNumber (number);

        return optionalSiteUser.get ();
    }

    public String getNumberByName(String number) {
        Optional<SiteUser> optionalSiteUser = siteUserRepository.findByNumber (number);
        SiteUser siteUser = optionalSiteUser.get ();
        return siteUser.getName ();
    }

    public List<Lesson> getLessonList(SiteUser siteUser) {
        List<Lesson> lessonList = siteUser.getLessonList ();
        return lessonList;
    }


    public SiteUser updateSalary(Long id, int salary, int bonus, int performancePay) {
        Optional<SiteUser> optionalUser = siteUserRepository.findById (id);
        if (optionalUser.isEmpty ()) {
            throw new IllegalArgumentException ("유저를 찾을 수 없습니다.");
        }
        SiteUser targetUser = optionalUser.get ();

        targetUser.setSalary (salary);
        targetUser.setBonus (bonus);
        targetUser.setPerformancePay (performancePay);

        return siteUserRepository.save (targetUser);
    }

    public List<SiteUser> getTrainerList() {
        return siteUserRepository.findByUserRole ("trainer");
    }

    public void findId(String userName, String birthDate, String email) {
        Optional<SiteUser> siteUserOptional = siteUserRepository.findByEmail (email);
        if (siteUserOptional.isPresent () && siteUserOptional.get ().getName ().equals (userName) && siteUserOptional.get ().getBirthDate ().equals (birthDate)) {
            SiteUser user = siteUserOptional.get ();
            String userId = user.getUserId ();

            mailService.mailSend (user.getEmail (), "아이디 찾기", "아이디 : " + userId);
        }
    }
}



