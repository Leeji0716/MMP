package com.example.MMP.siteuser;


//import com.example.MMP.mail.MailService;
import com.example.MMP.mail.MailService;
import com.example.MMP.security.UserDetail;
import com.example.MMP.wod.Wod;
import com.example.MMP.wod.WodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class SiteUserController {
    private final SiteUserService siteUserService;
    private final SiteUserRepository siteUserRepository;
    private final MailService mailService;
    private final WodService wodService;

    @GetMapping("/resetPassword")
    public String resetPasswordForm(Model model) {
        model.addAttribute("passwordResetRequestDto", new PasswordResetRequestDto());
        return "user/resetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@Valid PasswordResetRequestDto passwordResetRequestDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "user/resetPassword";
        }
        try {
            siteUserService.resetPassword(passwordResetRequestDto.getUserId(), passwordResetRequestDto.getEmail());
        } catch (Exception e) {
            bindingResult.reject("resetPasswordFailed", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "user/resetPassword";
        }
        return "redirect:/";
    }

    @GetMapping("/adminSignup")
    public String AdminSignup(AdminDto adminDto) {

        return "user/adminSignup";
    }

    @PostMapping("/adminSignup")
    public String AdminSignup(@Valid AdminDto adminDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/adminSignup";
        }
        try {
            siteUserService.adminSignup(adminDto.getName(), adminDto.getNumber(), adminDto.getGender(), adminDto.getBirthDate(), adminDto.getEmail());
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "user/adminSignup";
        } catch (Exception e) {
            bindingResult.reject("signupFailed", e.getMessage());
            return "user/adminSignup";
        }
        return "redirect:/";
    }


    @GetMapping("/login")
    public String login() {
        return "user/login_form";
    }

    @GetMapping("/signup")
    public String userSignup(UserDto userDto) {

        return "user/userSignup";
    }

    @PostMapping("/signup")
    public String userSignup(@Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/userSignup";
        }
        try {
            SiteUser siteUser = siteUserService.userSignup(userDto.getName(), userDto.getNumber(), userDto.getGender(), userDto.getBirthDate(), userDto.getEmail(), userDto.getUserRole());
            if(siteUser.getUserRole().equals("user"))
                mailService.mailSend(siteUser.getEmail()," [MMP] 회원가입을 환영합니다!","MMP의 회원이 되어주셔서 감사합니다!! 아이디는 전화번호, 비밀번호는 생년월일입니다..! 당신의 건강한 득근을 기원합니다 :)");
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "user/userSignup";
        } catch (Exception e) {
            bindingResult.reject("signupFailed", e.getMessage());
            return "user/userSignup";
        }

        return "redirect:/";

    }

    @GetMapping("/getUserID")
    public ResponseEntity<Map<String, Object>> getUserID(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        if (authentication != null && authentication.isAuthenticated()) {
            SiteUser user = (SiteUser) authentication.getPrincipal();
            response.put("userId", user.getId());
        } else {
            response.put("userId", null);
        }
        return ResponseEntity.ok(response);
    }


    @GetMapping("/changePassword")
    public String changePasswordForm(Model model) {
        model.addAttribute("passwordChangeDto", new PasswordChangeDto());
        return "user/changePassword";
    }

    @PostMapping("/changePassword")
    public String changePassword(@Valid PasswordChangeDto passwordChangeDto, BindingResult bindingResult, Authentication authentication, Model model) {
        if (bindingResult.hasErrors()) {
            return "user/changePassword";
        }
        try {
            UserDetail userDetail = (UserDetail) authentication.getPrincipal();
            String userId = userDetail.getUsername();
            Optional<SiteUser> optionalSiteUser = siteUserRepository.findByUserId(userId);
            SiteUser user = optionalSiteUser.get();
            siteUserService.changePassword(user.getId(), passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
        } catch (Exception e) {
            bindingResult.reject("changePasswordFailed", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "user/changePassword";
        }
        return "redirect:/";
    }

    @GetMapping("/profile")

    public String getUserProfile(Model model, Principal principal) {
        SiteUser user = this.siteUserService.getUser(principal.getName());
        List<Wod> wodList = wodService.findByUserWod(user);
        model.addAttribute("wodList",wodList);

        return "user/userProfile_form" ;
    }
}
