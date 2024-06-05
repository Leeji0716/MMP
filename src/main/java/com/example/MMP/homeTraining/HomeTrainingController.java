package com.example.MMP.homeTraining;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/homeTraining")
public class HomeTrainingController {
    private final HomeTrainingService homeTrainingService;

    @GetMapping("/home")
    public String main(){
        return "homeTraining/ht_main";
    }
}
