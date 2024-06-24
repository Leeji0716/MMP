package com.example.MMP.siteuser.salary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/salary")
public class SalaryController {

    private final SalaryService salaryService;

    @GetMapping("/create")
    public String showUpdateForm() {
        return "user/updateSalaryEtc"; // 폼을 보여줄 뷰의 이름입니다.
    }


    @PostMapping("/create")
    public String updateSalaryEtc(@RequestParam int bonus, @RequestParam int incentive, Model model) {
        try {
            salaryService.create(bonus, incentive);
            model.addAttribute("message", "모든 트레이너의 보너스와 인센티브가 업데이트되었습니다.");
        } catch (Exception e) {
            model.addAttribute("message", "업데이트 중 오류가 발생했습니다: " + e.getMessage());
        }
        return "user/updateSalaryEtc"; // 업데이트 후에도 같은 뷰를 반환합니다.
    }
}
