package com.example.MMP;

import com.example.MMP.information.Information;
import com.example.MMP.information.InformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final InformationService informationService;

    @GetMapping("/")
    public String mainPage(Model model){
        Information information = informationService.getInformation();
        model.addAttribute("information", information);
        return "schedule";
    }
}
