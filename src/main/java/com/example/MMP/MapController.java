package com.example.MMP;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapController {

    @GetMapping("/map")
    public String showMap() {
        return "map"; // map.html 뷰를 반환
    }

    @GetMapping("/heremap")
    public String hereMap() {
        return "heremap"; // map.html 뷰를 반환
    }

    @GetMapping("/game")
    public String game() {
        return "game";
    }

    @GetMapping("/main")
    public String schedule() {
        return "main";
    }

    @GetMapping("/coupon")
    public String coupon() {
        return "test";
    }
}