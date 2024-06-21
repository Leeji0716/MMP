package com.example.MMP.ptpass;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

        ptPassService.createPt(ptPassDto.getPassName(),ptPassDto.getPassTitle(),Integer.parseInt(ptPassDto.getPassCount()),Integer.parseInt(ptPassDto.getPassPrice()),Integer.parseInt(ptPassDto.getPassDays()));
        return "redirect:/";
    }
}