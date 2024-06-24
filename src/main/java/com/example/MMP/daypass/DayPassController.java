package com.example.MMP.daypass;

import com.example.MMP.ptpass.PtPass;
import com.example.MMP.ptpass.PtPassDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        dayPassService.create(dayPassDto.getDayPassName(),dayPassDto.getDayPassTitle(),Integer.parseInt(dayPassDto.getDayPassPrice()),Integer.parseInt(dayPassDto.getDayPassDays()));

        return "redirect:/totalPass/list";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Long id , DayPassDto dayPassDto, Model model){
        DayPass dayPass =dayPassService.findById(id);

        model.addAttribute("dayPass",dayPass);
        dayPassDto.setDayPassName(dayPass.getPassName());
        dayPassDto.setDayPassTitle(dayPass.getPassTitle());
        dayPassDto.setDayPassPrice(String.valueOf(dayPass.getPassPrice()));
        dayPassDto.setDayPassDays(String.valueOf(dayPass.getPassDays()));
        return "pass/daypassmake";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id , @Valid DayPassDto dayPassDto,BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "pass/ptpassmake";
        }
        DayPass dayPass = dayPassService.findById(id);
        dayPass.setPassName(dayPassDto.getDayPassName());
        dayPass.setPassTitle(dayPassDto.getDayPassTitle());
        dayPass.setPassPrice(Integer.parseInt(dayPassDto.getDayPassPrice()));
        dayPass.setPassDays(Integer.parseInt(dayPassDto.getDayPassDays()));
        dayPassService.save(dayPass);
        return "redirect:/totalPass/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
       DayPass dayPass = dayPassService.findById(id);
       dayPassService.delete(dayPass);

        return "redirect:/totalPass/list";
    }
}
