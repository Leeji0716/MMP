package com.example.MMP;

import com.example.MMP.challenge.attendance.Attendance;
import com.example.MMP.challenge.attendance.AttendanceRepository;
import com.example.MMP.challenge.attendance.AttendanceService;
import com.example.MMP.information.Information;
import com.example.MMP.information.InformationService;
import com.example.MMP.wod.OSType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

//    @GetMapping("ttt")
//    @ResponseBody
//    public List<SessionInformation> test() {
//        List<SessionInformation> lists = getSessions("01043004893");
//        List<Object> principals = sessionRegistry.getAllPrincipals();

//        for (Object principal : principals) {
//            if (principal instanceof UserDetails) {
//                UserDetails userDetails = (UserDetails) principal;
//                return sessionRegistry.getAllSessions(userDetails, false);
//            }
//        }

//        for (Object principal : principals) {
//            if (principal instanceof UserDetails) {
//                UserDetails userDetails = (UserDetails) principal;
//                return sessionRegistry.getAllSessions(userDetails, false);
//                if (userDetails.getUsername().equals("01043004893")) {
//                    return sessionRegistry.getAllSessions(userDetails, false);
//                }
//            }
//        }
//        return null;
//    }
}
