package com.example.MMP.totalpass;

import com.example.MMP.daypass.DayPass;
import com.example.MMP.daypass.DayPassService;
import com.example.MMP.ptpass.PtPass;
import com.example.MMP.ptpass.PtPassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/totalPass")
public class TotalPassController {
private final PtPassService ptPassService;
private final DayPassService dayPassService;
    @GetMapping("/list")
    public String PtPassList(Model model){
        List<PtPass> ptPassList = ptPassService.findAll();
        List<DayPass> dayPassList = dayPassService.findAll();
        model.addAttribute("ptPassList",ptPassList);
        model.addAttribute("dayPassList",dayPassList);
        return "pass/passList";
    }

}
