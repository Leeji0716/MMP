package com.example.MMP.totalpass;

import com.example.MMP.daypass.DayPass;
import com.example.MMP.daypass.DayPassService;
import com.example.MMP.ptpass.PtPass;
import com.example.MMP.ptpass.PtPassService;
import com.example.MMP.security.UserDetail;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.rmi.StubNotFoundException;
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

    @GetMapping("/list")
    public String PtPassList(Model model) {
        List<PtPass> ptPassList = ptPassService.findAll();
        List<DayPass> dayPassList = dayPassService.findAll();
        model.addAttribute("ptPassList", ptPassList);
        model.addAttribute("dayPassList", dayPassList);
        return "pass/passList";
    }

    @GetMapping("/transfer/{id}")
    public String transferPass(@PathVariable("id")Long id, @AuthenticationPrincipal UserDetail userDetail,Model model){
        SiteUser siteUser = siteUserService.getUser(userDetail.getUsername());
        SiteUser _siteUser = siteUserService.findById(id);
        if(siteUser != _siteUser){
            return "/";
        }
        model.addAttribute("user",siteUser);
        return "pass/transfer";
    }

    @PostMapping("/transfer/{id}")
    public ResponseEntity<?> handlePassSelection(@RequestBody Map<String, String> payload,@PathVariable("id")Long id) {
        String passName = payload.get("passName");
        PtPass ptPass = ptPassService.findByName(passName);
        if(ptPass == null){
            DayPass dayPass = dayPassService.findByName(passName);
            Map<String,String> response = new HashMap<>();
            response.put("passName",dayPass.getPassName());
            response.put("passTitle",dayPass.getPassTitle());
            response.put("passPrice",Integer.toString(dayPass.getPassPrice()));
            response.put("passDays",Integer.toString(dayPass.getPassDays()));

            return ResponseEntity.ok().body(response);
        }else{
            Map<String,String> response = new HashMap<>();
            response.put("passName", ptPass.getPassName());
            response.put("passTitle", ptPass.getPassTitle());
            response.put("passCount",Integer.toString(ptPass.getPassCount()));
            response.put("passPrice",Integer.toString(ptPass.getPassPrice()));
            response.put("passDays",Integer.toString(ptPass.getPassDays()));

            return ResponseEntity.ok().body(response);
        }

    }
}