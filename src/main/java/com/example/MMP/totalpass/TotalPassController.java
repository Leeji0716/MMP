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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.rmi.StubNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.MMP.ptpass.QPtPass.ptPass;

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
        model.addAttribute("user", siteUser);
        return "pass/transfer";
    }

    @PostMapping("/transfer/{id}")
    public ResponseEntity<?> handlePassSelection(@RequestBody Map<String, String> payload, @PathVariable("id") Long id) {
        String passName = payload.get("passName");
        UserPtPass userPtPass = userPtPassService.findByPassName(passName);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (userPtPass == null) {
            UserDayPass userDayPass = userDayPassService.findByPassName(passName);
            Map<String, String> response = new HashMap<>();
            response.put("passName", userDayPass.getPassName());
            response.put("passStart", userDayPass.getPassStart().format(formatter));
            response.put("passFinish",userDayPass.getPassFinish().format(formatter));

            return ResponseEntity.ok().body(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("passName", userPtPass.getPassName());
            response.put("passCount", Integer.toString(userPtPass.getPassCount()));
            response.put("passStart", userPtPass.getPassStart().format(formatter));
            response.put("passFinish",userPtPass.getPassFinish().format(formatter));

            return ResponseEntity.ok().body(response);
        }

    }

    @GetMapping("/transfer/success")
    public String agree(@RequestParam("number")String number,@RequestParam("pass")String pass,@AuthenticationPrincipal UserDetail userDetail){
        SiteUser sendUser = siteUserService.getUser(userDetail.getUsername());
        SiteUser acceptUser = siteUserService.getUser(number);
        UserPtPass userPtPass = userPtPassService.findByPassName(pass);
        if(userPtPass != null){
            TransPass transPass = TransPass.builder().sendUser(sendUser).acceptUser(acceptUser).build();
            transPass.setUserPtPass(userPtPass);
            transPassService.save(transPass);
        }else{
            UserDayPass userDayPass = userDayPassService.findByPassName(pass);
            TransPass transPass = TransPass.builder().sendUser(sendUser).acceptUser(acceptUser).build();
            transPass.setUserDayPass(userDayPass);
            transPassService.save(transPass);
        }

        return "redirect:/";
    }
}