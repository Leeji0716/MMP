package com.example.MMP.siteuser;


//import com.example.MMP.mail.MailService;
import com.example.MMP.Comment.Comment;
import com.example.MMP.Comment.CommentService;
import com.example.MMP.challenge.challenge.Challenge;
import com.example.MMP.challenge.challenge.ChallengeService;
import com.example.MMP.challenge.challengeUser.ChallengeUser;
import com.example.MMP.daypass.DayPass;
import com.example.MMP.daypass.DayPassService;
import com.example.MMP.homeTraining.HomeTraining;
import com.example.MMP.homeTraining.HomeTrainingService;
import com.example.MMP.mail.MailService;
import com.example.MMP.ptpass.PtPass;
import com.example.MMP.ptpass.PtPassService;
import com.example.MMP.security.UserDetail;
import com.example.MMP.transPass.TransPass;
import com.example.MMP.transPass.TransPassService;
import com.example.MMP.userPass.UserDayPass;
import com.example.MMP.userPass.UserDayPassService;
import com.example.MMP.userPass.UserPtPass;
import com.example.MMP.userPass.UserPtPassService;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class SiteUserController {
    private final SiteUserService siteUserService;
    private final SiteUserRepository siteUserRepository;
    private final MailService mailService;
    private final WodService wodService;
    private final HomeTrainingService homeTrainingService;
    private final CommentService commentService;
    private final ChallengeService challengeService;
    private final TransPassService transPassService;

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
    public String getUserProfile(Model model, Principal principal, @RequestParam(value = "passFilter",defaultValue = "pt") String passFilter) {

        try {
            SiteUser user = this.siteUserService.getUser (principal.getName ());
            List<Wod> wodList = wodService.findByUserWod (user);
            List<HomeTraining> saveTraining = homeTrainingService.getSaveTraining (user);
            int points = user.getPoint ().getPoints ();

            List<Comment> commentList;
            List<Comment> topComment = new ArrayList<> ();
            for (Wod wod : wodList) {
                commentList = commentService.getCommentsByWodOrderByCreateDateDesc (wod);
                topComment.addAll (commentList);
            }

            Map<String, List<Challenge>> challengesByStatus = challengeService.getChallengesByStatus (user.getId ());
            List<Challenge> ongoingChallenges = challengesByStatus.get ("ongoing");
            List<Challenge> successfulChallenges = challengesByStatus.get ("successful");
            List<Challenge> failedChallenges = challengesByStatus.get ("failed");
            int challengeCount = ongoingChallenges.size () + successfulChallenges.size () + failedChallenges.size ();

            if(passFilter.equals("pt")) {
                List<TransPass> MySendPass = transPassService.MySendPass(user);
                if(MySendPass == null){
                    model.addAttribute("MySendPass",null);
                }
                model.addAttribute("MySendPass",MySendPass);
            }else{
                List<TransPass> MyAcceptPass = transPassService.MyAcceptPass(user);
                if(MyAcceptPass == null){
                    model.addAttribute("MyAcceptPass",null);
                }
                model.addAttribute("MyAcceptPass",MyAcceptPass);
            }


            model.addAttribute ("wodList", wodList);
            model.addAttribute ("saveTraining", saveTraining);
            model.addAttribute ("user", user);
            model.addAttribute ("points", points);
            model.addAttribute ("topSevenComment", commentService.getTop7Comments (topComment));
            model.addAttribute ("ongoingChallenges", ongoingChallenges);
            model.addAttribute ("successfulChallenges", successfulChallenges);
            model.addAttribute ("failedChallenges", failedChallenges);
            model.addAttribute ("challengeCount", challengeCount);

            return "user/userProfile_form";
        } catch (Exception e) {
            model.addAttribute ("errorMessage", "프로필 정보를 불러오는 중 오류가 발생했습니다.");
            return "error";
        }
    }
}
 