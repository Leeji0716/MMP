package com.example.MMP.daypass;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/day")
public class DayPassController {
    private final DayPassService dayPassService;

    @GetMapping("/add")
    public String dayadd(DayPassDto dayPassDto){


        return "pass/daypassmake";
    }

    @PostMapping("/add")
    public String dayadd(@Valid DayPassDto dayPassDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "pass/daypassmake";
        }
        dayPassService.create(dayPassDto.getDayPassName(),dayPassDto.getDayPassTitle(),dayPassDto.getDayPassPrice(),dayPassDto.getDayPassDays());

        return "redirect:/totalPass/list";
    }
}
