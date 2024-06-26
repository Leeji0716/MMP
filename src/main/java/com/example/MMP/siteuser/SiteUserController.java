package com.example.MMP.siteuser;


//import com.example.MMP.mail.MailService;

import com.example.MMP.challenge.challenge.Challenge;
import com.example.MMP.challenge.challenge.ChallengeService;
import com.example.MMP.chat.ChatRoom;
import com.example.MMP.chat.ChatRoomDto;
import com.example.MMP.chat.ChatRoomService;
import com.example.MMP.homeTraining.HomeTraining;
import com.example.MMP.homeTraining.HomeTrainingService;
import com.example.MMP.mail.MailService;
import com.example.MMP.ptGroup.PtGroup;
import com.example.MMP.ptGroup.PtGroupRepository;
import com.example.MMP.security.UserDetail;
import com.example.MMP.siteuser.salary.Salary;
import com.example.MMP.siteuser.salary.SalaryRepository;
import com.example.MMP.transPass.TransPass;
import com.example.MMP.transPass.TransPassService;
import com.example.MMP.wod.Comment.Comment;
import com.example.MMP.wod.Comment.CommentService;
import com.example.MMP.wod.Wod;
import com.example.MMP.wod.WodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    private final PtGroupRepository ptGroupRepository;
    private final ChatRoomService chatRoomService;
    private final SalaryRepository salaryRepository;


    @GetMapping("/resetPassword")
    public String resetPasswordForm(Model model) {
        model.addAttribute ("passwordResetRequestDto", new PasswordResetRequestDto ());
        return "user/resetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@Valid PasswordResetRequestDto passwordResetRequestDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors ()) {
            return "user/resetPassword";
        }
        try {
            siteUserService.resetPassword (passwordResetRequestDto.getUserId (), passwordResetRequestDto.getEmail ());
        } catch (Exception e) {
            bindingResult.reject ("resetPasswordFailed", e.getMessage ());
            model.addAttribute ("errorMessage", e.getMessage ());
            return "user/resetPassword";
        }
        return "redirect:/";
    }

    @GetMapping("/adminSignup")
    public String AdminSignup(AdminDto adminDto) {

        return "user/adminSignup";
    }

    @GetMapping("/commandcenter")
    public String commandcenter() {
        return "commandcenter";
    }

    @PostMapping("/adminSignup")
    public String AdminSignup(@Valid AdminDto adminDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors ()) {
            return "user/adminSignup";
        }
        try {
            siteUserService.adminSignup (adminDto.getName (), adminDto.getNumber (), adminDto.getGender (), adminDto.getBirthDate (), adminDto.getEmail ());
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject ("signupFailed", "이미 등록된 사용자입니다.");
            return "user/adminSignup";
        } catch (Exception e) {
            bindingResult.reject ("signupFailed", e.getMessage ());
            return "user/adminSignup";
        }
        return "redirect:/";
    }


    @GetMapping("/login")
    public String login() {
        return "user/login_form";
    }

    @GetMapping("/signup")
    public String userSignup(UserDto userDto, Model model) {
        List<SiteUser> trainers = siteUserRepository.findByUserRole ("trainer");
        model.addAttribute ("trainers", trainers);
        model.addAttribute ("userDto", userDto);

        return "user/userSignup";
    }

    @PostMapping("/signup")
    public String userSignup(@Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors ()) {
            return "user/userSignup";
        }
        try {
            SiteUser siteUser = siteUserService.userSignup (userDto.getName (), userDto.getNumber (), userDto.getGender (), userDto.getBirthDate (), userDto.getEmail (), userDto.getUserRole (), userDto.getSalary (),
                    userDto.getReferrerId ());
            if (siteUser.getUserRole ().equals ("user")) {
                mailService.mailSend (siteUser.getEmail (), " [MMP] 회원가입을 환영합니다!", "MMP의 회원이 되어주셔서 감사합니다!! 아이디는 전화번호, 비밀번호는 생년월일입니다..! 당신의 건강한 득근을 기원합니다 :)");
            } else if (siteUser.getUserRole ().equals ("trainer")) {
                PtGroup ptGroup = new PtGroup ();
                siteUser.setPtGroupTrainer (ptGroup);
                ptGroupRepository.save (ptGroup);
                siteUserService.save (siteUser);
            }
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject ("signupFailed", "이미 등록된 사용자입니다.");
            return "user/userSignup";
        } catch (Exception e) {
            bindingResult.reject ("signupFailed", e.getMessage ());
            return "user/userSignup";
        }
        return "redirect:/";
    }


    @GetMapping("/getUserID")
    public ResponseEntity<Map<String, Object>> getUserID(Authentication authentication) {
        Map<String, Object> response = new HashMap<> ();
        if (authentication != null && authentication.isAuthenticated ()) {
            SiteUser user = (SiteUser) authentication.getPrincipal ();
            response.put ("userId", user.getId ());
        } else {
            response.put ("userId", null);
        }
        return ResponseEntity.ok (response);
    }


    @GetMapping("/changePassword")
    public String changePasswordForm(Model model) {
        model.addAttribute ("passwordChangeDto", new PasswordChangeDto ());
        return "user/changePassword";
    }

    @PostMapping("/changePassword")
    public String changePassword(@Valid PasswordChangeDto passwordChangeDto, BindingResult bindingResult, Authentication authentication, Model model) {
        if (bindingResult.hasErrors ()) {
            return "user/changePassword";
        }
        try {
            UserDetail userDetail = (UserDetail) authentication.getPrincipal ();
            String userId = userDetail.getUsername ();
            Optional<SiteUser> optionalSiteUser = siteUserRepository.findByUserId (userId);
            SiteUser user = optionalSiteUser.get ();
            siteUserService.changePassword (user.getId (), passwordChangeDto.getCurrentPassword (), passwordChangeDto.getNewPassword ());
        } catch (Exception e) {
            bindingResult.reject ("changePasswordFailed", e.getMessage ());
            model.addAttribute ("errorMessage", e.getMessage ());
            return "user/changePassword";
        }
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String getUserProfile(Model model, Principal principal, @RequestParam(value = "passFilter", defaultValue = "양도 신청 리스트") String passFilter) {

        try {
            SiteUser user = this.siteUserService.getUser(principal.getName ());
            List<Wod> wodListAll = wodService.getWodListByCreateDateDesc(user);
            List<HomeTraining> saveTraining = homeTrainingService.getSaveTraining (user);
            int points = user.getPoint ().getPoints ();

            List<Comment> commentList;
            List<Comment> topComment = new ArrayList<> ();
            for (Wod wod : wodListAll) {
                commentList = commentService.getCommentsByWodOrderByCreateDateDesc (wod);
                topComment.addAll (commentList);
            }

            Map<String, List<Challenge>> challengesByStatus = challengeService.getChallengesByStatus (user.getId ());
            List<Challenge> ongoingChallenges = challengesByStatus.get ("ongoing");
            List<Challenge> successfulChallenges = challengesByStatus.get ("successful");
            List<Challenge> failedChallenges = challengesByStatus.get ("failed");
            int challengeCount = ongoingChallenges.size () + successfulChallenges.size () + failedChallenges.size ();

            if (passFilter.equals ("양도 신청 리스트")) {
                List<TransPass> MySendPass = transPassService.MySendPass (user);
                if (MySendPass.isEmpty ()) {
                    model.addAttribute ("MySendPass", null);
                }
                model.addAttribute ("MySendPass", MySendPass);
            } else if (passFilter.equals ("양도 머기 리스트")) {
                List<TransPass> MyAcceptPass = transPassService.MyAcceptPass (user);
                if (MyAcceptPass.isEmpty ()) {
                    model.addAttribute ("MyAcceptPass", null);
                }
                model.addAttribute ("MyAcceptPass", MyAcceptPass);
            } else if (passFilter.equals ("관리자 승인")) {
                List<TransPass> MyStandPass = transPassService.MyStandPass (user);
                if (MyStandPass.isEmpty ()) {
                    model.addAttribute ("MyStandPass", null);
                }
                model.addAttribute ("MyStandPass", MyStandPass);
            }

            Map<String, List<ChatRoomDto>> chatDto = chatRoomService.findChat (user);
            if (chatDto == null) {
                model.addAttribute ("chatDto", null);
            }


            model.addAttribute ("topTenWodList", wodService.getTop10Wods(wodListAll));
            model.addAttribute ("saveTraining", saveTraining);
            model.addAttribute ("user", user);
            model.addAttribute ("points", points);
            model.addAttribute ("topSevenComment", commentService.getTop7Comments (topComment));
            model.addAttribute ("ongoingChallenges", ongoingChallenges);
            model.addAttribute ("successfulChallenges", successfulChallenges);
            model.addAttribute ("failedChallenges", failedChallenges);
            model.addAttribute ("chatDto", chatDto);
            model.addAttribute ("challengeCount", challengeCount);

            return "user/userProfile_form";
        } catch (Exception e) {
            model.addAttribute ("errorMessage", "프로필 정보를 불러오는 중 오류가 발생했습니다.");
            return "error";
        }
    }

    @GetMapping("/user/{loginId}")
    public ResponseEntity<Optional<SiteUser>> getUser(@PathVariable String userId) {
        Optional<SiteUser> siteUser = siteUserRepository.findByUserId (userId);
        if (siteUser.isEmpty ()) {
            return ResponseEntity.status (HttpStatus.NOT_FOUND).body (null);
        }
        return ResponseEntity.ok (siteUser);
    }

    @GetMapping("/chat/{id}")
    public String chat(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetail userDetail, Model model) {
        SiteUser siteUser = siteUserService.getUser (userDetail.getUsername ());
        SiteUser siteUser1 = siteUserService.findById (id);
        ChatRoom chatRoom = chatRoomService.findChatroom (siteUser, siteUser1);
        chatRoomService.deleteAlarm (siteUser, chatRoom);
        model.addAttribute ("chatRoom", chatRoom);
        model.addAttribute ("me", siteUser);
        model.addAttribute ("you", siteUser1);

        return "chat/chatroom";
    }

    @GetMapping("/trainerList")
    public String trainerList(Model model, Principal principal) {

        List<SiteUser> trainers = siteUserRepository.findByUserRole ("trainer");

        model.addAttribute ("trainers", trainers);

        return "user/trainerList";

    }

    @GetMapping("/userList")
    public String userList(Model model, Principal principal) {

        List<SiteUser> users = siteUserRepository.findByUserRole ("user");

        model.addAttribute ("users", users);

        return "user/userList";

    }

    @GetMapping("/salaryForm")
    public String showSalaryForm(@RequestParam String number, Model model) {
        Optional<SiteUser> optionalSiteUser = siteUserRepository.findByNumber (number);

        SiteUser siteUser = optionalSiteUser.get ();

        long referralCount = siteUserRepository.countByReferrer (siteUser);

        List<Salary> salaries = salaryRepository.findAll ();

        Salary lastSalary = null;
        if (!salaries.isEmpty ()) {
            lastSalary = salaries.get (salaries.size () - 1);
        }

        model.addAttribute ("siteUser", siteUser);
        model.addAttribute ("number", siteUser.getNumber ());
        model.addAttribute ("userName", siteUser.getName ());
        model.addAttribute ("referralCount", referralCount);
        model.addAttribute ("lastSalary", lastSalary);

        return "user/salaryForm";
    }

    @PostMapping("/createSalary")
    public String createSalary(@RequestParam String number, @RequestParam int salary, @RequestParam int bonus, @RequestParam int referralCount, @RequestParam int incentive, Model model) {
        int performancePay = referralCount * incentive;

        Long id = siteUserService.findByNumber (number).getId ();
        SiteUser sumSalary = siteUserService.updateSalary (id, salary, bonus, performancePay);

        SiteUser siteUser = siteUserService.findById (id);

        model.addAttribute ("number", siteUser.getNumber ());
        model.addAttribute ("sumSalary", sumSalary);
        model.addAttribute ("user", siteUser);
        return "redirect:/user/salaryDetail?number=" + number;
    }

    @GetMapping("/salaryDetail")
    public String showSalaryDetail(@RequestParam String number, Model model) {

        SiteUser siteUser = siteUserService.findByNumber (number);

        long referralCount = siteUserRepository.countByReferrer (siteUser);

        long sumSalary = siteUser.getSalary () + siteUser.getBonus () + (referralCount * siteUser.getPerformancePay ());

        model.addAttribute ("sumSalary", sumSalary);
        model.addAttribute ("referralCount", referralCount);
        model.addAttribute ("user", siteUser);
        return "user/salaryDetail";
    }

    @GetMapping("/findId")
    public String findId(Model model) {
        model.addAttribute ("findIdDto", new FindIdDto ());
        return "user/findIdForm";
    }

    @PostMapping("/findId")
    public String findId(@Valid FindIdDto findIdDto, BindingResult bindingResult, Authentication authentication, Model model) {
        if (bindingResult.hasErrors ()) {
            return "user/findIdForm";
        }
        try {
            siteUserService.findId (findIdDto.getName (),findIdDto.getBirthDate (),findIdDto.getEmail ());
        } catch (Exception e) {
            bindingResult.reject ("findIdDto", e.getMessage ());
            model.addAttribute ("errorMessage", e.getMessage ());
            return "user/findIdForm";
        }
        return "redirect:/";
    }

}
 