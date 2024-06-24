package com.example.MMP.ptpass;

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
        return "redirect:/totalPass/list";
    }


    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Long id , PtPassDto ptPassDto, Model model){
        PtPass ptPass =ptPassService.findById(id);

        model.addAttribute("ptPass",ptPass);
        ptPassDto.setPassName(ptPass.getPassName());
        ptPassDto.setPassTitle(ptPass.getPassTitle());
        ptPassDto.setPassDays(String.valueOf(ptPass.getPassDays()));
        ptPassDto.setPassPrice(String.valueOf(ptPass.getPassPrice()));
        ptPassDto.setPassCount(String.valueOf(ptPass.getPassCount()));
        return "pass/ptpassmake";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id , @Valid PtPassDto ptPassDto,BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "pass/ptpassmake";
        }
        PtPass ptPass =ptPassService.findById(id);
        ptPass.setPassName(ptPassDto.getPassName());
        ptPass.setPassTitle(ptPassDto.getPassTitle());
        ptPass.setPassCount(Integer.parseInt(ptPassDto.getPassCount()));
        ptPass.setPassPrice(Integer.parseInt(ptPassDto.getPassPrice()));
        ptPass.setPassDays(Integer.parseInt(ptPassDto.getPassDays()));
        ptPassService.save(ptPass);

        return "redirect:/totalPass/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        PtPass ptPass = ptPassService.findById(id);
        ptPassService.delete(ptPass);

        return "redirect:/totalPass/list";
    }
}