package com.example.MMP.ptpass;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/pt")
public class PtPassController {
private final PtPassService ptPassService;
    @GetMapping("/add")
    public String ptadd(PtPassDto ptPassDto){

        return "pass/ptpassmake";
    }

    @PostMapping("/add")
    public String ptadd(@Valid PtPassDto ptPassDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "pass/ptpassmake";
        }
        ptPassService.createPt(ptPassDto.getPassName(),ptPassDto.getPassTitle(),ptPassDto.getPassCount(),ptPassDto.getPassPrice());
        return "redirect:/";
    }

    @GetMapping("/list")
    public String PtPassList(Model model){
        List<PtPass> ptPassList = ptPassService.findAll();
        model.addAttribute("ptPassList",ptPassList);
        return "pass/passList";
    }
}
