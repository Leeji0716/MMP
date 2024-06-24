package com.example.MMP;

import com.example.MMP.challenge.attendance.Attendance;
import com.example.MMP.challenge.attendance.AttendanceRepository;
import com.example.MMP.challenge.attendance.AttendanceService;
import com.example.MMP.information.Information;
import com.example.MMP.information.InformationService;
import com.example.MMP.wod.OSType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final InformationService informationService;
    private final AttendanceRepository attendanceRepository;

    @GetMapping("/")
    public String mainPage(Model model) {
        Information information = informationService.getInformation ();

        List<Attendance> attendances = attendanceRepository.findAll ();
        int presentCount = 0;

        if (!attendances.isEmpty ()) {
            for (Attendance attendance : attendances) {
                if (attendance.isPresent () == true) {
                    presentCount++;
                }
            }
        }


        model.addAttribute ("information", information);
        model.addAttribute ("presentCount", presentCount);
        return "schedule";
    }
}
