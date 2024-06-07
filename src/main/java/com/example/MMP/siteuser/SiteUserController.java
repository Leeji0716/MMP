package com.example.MMP.siteuser;


import com.example.MMP.security.UserDetail;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class SiteUserController {
    private final SiteUserService siteUserService;
    private final SiteUserRepository siteUserRepository;

    @GetMapping("/adminSignup")
    public String AdminSignup(AdminDto adminDto){

        return "user/adminSignup";
    }

    @PostMapping("/adminSignup")
    public String AdminSignup(@Valid AdminDto adminDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
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
    public String userSignup(UserDto userDto){

        return "user/userSignup";
    }

    @PostMapping("/signup")
    public String userSignup(@Valid UserDto userDto,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "user/userSignup";
        } try {
            siteUserService.userSignup(userDto.getName(),userDto.getNumber(),userDto.getGender(),userDto.getBirthDate(),userDto.getEmail(),userDto.getUserRole());
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
            String userId = userDetail.getUsername ();
            Optional<SiteUser> optionalSiteUser = siteUserRepository.findByUserId (userId);
            SiteUser user = optionalSiteUser.get ();
            siteUserService.changePassword(user.getId(), passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
        } catch (Exception e) {
            bindingResult.reject("changePasswordFailed", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "user/changePassword";
        }
        return "redirect:/";
    }
}
