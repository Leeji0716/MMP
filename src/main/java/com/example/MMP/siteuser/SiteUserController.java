package com.example.MMP.siteuser;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class SiteUserController {
    private final SiteUserService siteUserService;

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
}
