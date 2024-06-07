package com.example.MMP;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class sogae {

    @GetMapping("/map")
    public String map(){

        return "Nearby_businesses";
    }
}
