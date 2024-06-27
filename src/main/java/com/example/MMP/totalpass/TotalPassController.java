package com.example.MMP.totalpass;

import com.example.MMP.daypass.DayPass;
import com.example.MMP.daypass.DayPassService;
import com.example.MMP.ptpass.PtPass;
import com.example.MMP.ptpass.PtPassService;
import com.example.MMP.security.UserDetail;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserService;
import com.example.MMP.transPass.TransPass;
import com.example.MMP.transPass.TransPassService;
import com.example.MMP.userPass.UserDayPass;
import com.example.MMP.userPass.UserDayPassService;
import com.example.MMP.userPass.UserPtPass;
import com.example.MMP.userPass.UserPtPassService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequiredArgsConstructor
@RequestMapping("/totalPass")
public class TotalPassController {
    private final PtPassService ptPassService;
    private final DayPassService dayPassService;
    private final SiteUserService siteUserService;
    private final UserPtPassService userPtPassService;
    private final UserDayPassService userDayPassService;
    private final TransPassService transPassService;

    @GetMapping("/list")
    public String PtPassList(Model model) {
        List<PtPass> ptPassList = ptPassService.findAll();
        List<DayPass> dayPassList = dayPassService.findAll();
        model.addAttribute("ptPassList", ptPassList);
        model.addAttribute("dayPassList", dayPassList);

        return "pass/passList";
    }

    @GetMapping("/transfer/{id}")
    public String transferPass(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetail userDetail, Model model) {
        SiteUser siteUser = siteUserService.getUser(userDetail.getUsername());
        SiteUser _siteUser = siteUserService.findById(id);
        if (siteUser != _siteUser) {
            return "/";
        }

        List<TransPass> MySendPass = transPassService.MySendPass (siteUser);
        if (MySendPass.isEmpty ()) {
            model.addAttribute ("MySendPass", null);
        }
        model.addAttribute ("MySendPass", MySendPass);

        List<TransPass> MyAcceptPass = transPassService.MyAcceptPass (siteUser);
        if (MyAcceptPass.isEmpty ()) {
            model.addAttribute ("MyAcceptPass", null);
        }
        model.addAttribute ("MyAcceptPass", MyAcceptPass);

//        List<TransPass> MyStandPass = transPassService.MyStandPass (siteUser);
//        if (MyStandPass.isEmpty ()) {
//            model.addAttribute ("MyStandPass", null);
//        }
//        model.addAttribute ("MyStandPass", MyStandPass);

        model.addAttribute("user", siteUser);
        return "pass/transfer";
    }

    @PostMapping("/transfer/{id}")
    public ResponseEntity<?> handlePassSelection(@RequestBody Map<String, String> payload, @PathVariable("id") Long id) {
        String passSort = payload.get("passSort");
        Long passId = Long.valueOf(payload.get("passId"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(passSort.equals("Pt")){
            UserPtPass userPtPass = userPtPassService.findByPassId(passId);
            Map<String, String> response = new HashMap<>();
            response.put("passName", userPtPass.getPassName());
            response.put("passCount", Integer.toString(userPtPass.getPassCount()));
            response.put("passStart", userPtPass.getPassStart().format(formatter));
            response.put("passFinish", userPtPass.getPassFinish().format(formatter));
            response.put("passSort",passSort);
            return ResponseEntity.ok().body(response);
        }else{
            UserDayPass userDayPass = userDayPassService.findById(passId);
            Map<String, String> response = new HashMap<>();
            response.put("passName", userDayPass.getPassName());
            response.put("passStart", userDayPass.getPassStart().format(formatter));
            response.put("passFinish", userDayPass.getPassFinish().format(formatter));
            response.put("passSort",passSort);
            return ResponseEntity.ok().body(response);
        }
    }

    @GetMapping("/transfer/success")
    public String agree(@RequestParam(value = "number",defaultValue = "error") String number, @RequestParam(value = "pass",defaultValue = "0") Long pass, @RequestParam(value = "sort",defaultValue = "error") String sort, @AuthenticationPrincipal UserDetail userDetail, RedirectAttributes redirectAttributes) {
        SiteUser sendUser = siteUserService.getUser(userDetail.getUsername());
        if(number.equals("error")){
            redirectAttributes.addFlashAttribute("error","양도받을 회원을 선택해주세요.");
            return "redirect:/totalPass/transfer/%d".formatted(sendUser.getId());
        } else if (pass == 0) {
            redirectAttributes.addFlashAttribute("error","양도할 이용권을 선택해주세요.");
            return "redirect:/totalPass/transfer/%d".formatted(sendUser.getId());
        } else if(sort.equals("error")){
            redirectAttributes.addFlashAttribute("error","양도할 이용권을 선택해주세요.");
            return "redirect:/totalPass/transfer/%d".formatted(sendUser.getId());
        }
        SiteUser acceptUser = siteUserService.getUser(number);


        if(sort.equals("Pt")){
            UserPtPass userPtPass = userPtPassService.findByPassId(pass);
            TransPass transPass = TransPass.builder().sendUser(sendUser).acceptUser(acceptUser).build();
            transPass.setUserPtPass(userPtPass);
            List<TransPass> transPassList = transPassService.MySendPass(sendUser);
            for(TransPass transPass1 : transPassList){
                if(transPass1.getUserPtPass() == transPass.getUserPtPass() ){
                    redirectAttributes.addFlashAttribute("error","이미 양도신청한 이용권입니다.");
                    return "redirect:/totalPass/transfer/%d".formatted(sendUser.getId());
                }
            }
            transPassService.save(transPass);
        }else{
            UserDayPass userDayPass = userDayPassService.findById(pass);
            TransPass transPass = TransPass.builder().sendUser(sendUser).acceptUser(acceptUser).build();
            transPass.setUserDayPass(userDayPass);
            List<TransPass> transPassList = transPassService.MySendPass(sendUser);
            for(TransPass transPass1 : transPassList){
                if(transPass1.getUserDayPass() == transPass.getUserDayPass()){
                    redirectAttributes.addFlashAttribute("error","이미 양도신청한 이용권입니다.");
                    return "redirect:/totalPass/transfer/%d".formatted(sendUser.getId());
                }
            }
            transPassService.save(transPass);
        }

        return "redirect:/";
    }

    @PostMapping("/change/{id}")
    public String changePt(@RequestParam("change") String change, @PathVariable("id") Long id) {
        if (change.equals("accept")) {
            TransPass transPass = transPassService.findById(id);
            transPass.setConsent(true);
            transPassService.save(transPass);
            return "redirect:/user/profile";
        } else {
            TransPass transPass = transPassService.findById(id);
            transPassService.delete(transPass);
            return "redirect:/user/profile";
        }
    }

    @GetMapping("/admin/stand")
    public String adminStand(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "sort", defaultValue = "pt") String sort) {
        if (sort.equals("pt")) {
            Page<TransPass> paging = transPassService.AllPtStandPass(page);
            model.addAttribute("paging", paging);
            return "pass/standpass";
        } else {
            Page<TransPass> paging = transPassService.AllDayStandPass(page);
            model.addAttribute("paging", paging);
            return "pass/standpass";
        }
    }

    @GetMapping("/admin/agree/{id}")
    public String adminAgree(@PathVariable("id") Long id) {
        TransPass transPass = transPassService.findById(id);
        if (transPass.getUserPtPass() != null) {
            transPass.getUserPtPass().setSiteUser(transPass.getAcceptUser());
            userPtPassService.save(transPass.getUserPtPass());
            transPassService.delete(transPass);
            return "redirect:/totalPass/admin/stand";
        } else {
            transPass.getUserDayPass().setSiteUser(transPass.getAcceptUser());
            userDayPassService.save(transPass.getUserDayPass());
            transPassService.delete(transPass);
            return "redirect:/totalPass/admin/stand";
        }

    }

    @GetMapping("/admin/reject/{id}")
    public String adminReject(@PathVariable("id") Long id) {
        TransPass transPass = transPassService.findById(id);
        transPassService.delete(transPass);

        return "redirect:/totalPass/admin/stand";
    }

}