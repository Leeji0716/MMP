package com.example.MMP.homeTraining;

import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserService;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/homeTraining")
public class HomeTrainingController {
    private final HomeTrainingService homeTrainingService;
    private final SiteUserService siteUserService;

    @GetMapping("/home")
    public String main(Model model){
        List<HomeTraining> homeTrainingList = homeTrainingService.getList();
        model.addAttribute("homeTrainingList", homeTrainingList);

        return "homeTraining/ht_main";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    private String create(HomeTrainingForm homeTrainingForm){
        return "homeTraining/ht_create";
    }

    @PostMapping("/create")
    private String create(@Valid HomeTrainingForm homeTrainingForm, BindingResult bindingResult, Principal principal){
        if(bindingResult.hasErrors()){
            return "homeTraining/ht_create";
        }
        SiteUser writer = siteUserService.getUser(principal.getName());
        homeTrainingService.create(homeTrainingForm.getContent(), homeTrainingForm.getVideoUrl(), writer);

        return "redirect:/homeTraining/home";
    }
}
