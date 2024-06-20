package com.example.MMP.lesson;

import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserService;
import com.example.MMP.userPass.UserPtPass;
import com.example.MMP.userPass.UserPtPassService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@EnableScheduling
@RequestMapping("/lesson")
public class LessonController {
    private final LessonService lessonService;
    private final SiteUserService siteUserService;



    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    private String create(LessonForm lessonForm){
        return "lesson/lesson_create";
    }

    @PostMapping("/create")
    private String create(@Valid LessonForm lessonForm, BindingResult bindingResult, Principal principal){

        if(bindingResult.hasErrors()){
            return "lesson/lesson_create";
        }
        SiteUser trainer = siteUserService.getUser(principal.getName());

        lessonService.create(lessonForm.getLessonName(), lessonForm.getHeadCount(), lessonForm.getLessonDate(), lessonForm.getStartTime(), lessonForm.getEndTime(), trainer);

        return "redirect:/";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model, Principal principal){
        Lesson lesson = lessonService.getLesson(id);
        String currentUsername = principal.getName();

        SiteUser user = siteUserService.getUser(currentUsername);

        boolean isUserAttending = lesson.getAttendanceList().stream()
                .anyMatch(attendant -> attendant.getUserId().equals(currentUsername));

        boolean isLessonAttended;

        if(isUserAttending){//예약신청이 된 상태,
            if (lesson.getAttendanceList().size() <= lesson.getHeadCount()){ //예약리스트길이가 인원수와 같거나 작을 때. --> 예약 완료
                isLessonAttended = true;
            }else { //예약리스트길이가 인원수보다 클 때. --> 대기 완료
                isLessonAttended = false;
            }
        }else {//예약신청이 안된 상태,
            if (!(lesson.getAttendanceList().size() < lesson.getHeadCount())){//예약리스트길이가 인원수와 같거나 클 때. --> 대기 하기
                isLessonAttended = false;
            }else { //예약리스트길이가 인원수보다 작을 때 --> 예약 하기
                isLessonAttended = true;
            }
        }
        List<SiteUser> reservationList = new ArrayList<>();
        List<SiteUser> waitingList = new ArrayList<>();
        for(int i = 0; i < lesson.getAttendanceList().size(); i++){
            if (i < lesson.getHeadCount()){
                reservationList.add(lesson.getAttendanceList().get(i));
            }else {
                waitingList.add(lesson.getAttendanceList().get(i));
            }
        }

        model.addAttribute("lesson", lesson);
        model.addAttribute("isUserAttending", isUserAttending);
        model.addAttribute("isLessonAttended", isLessonAttended);
        model.addAttribute("reservationList", reservationList);
        model.addAttribute("waitingList", waitingList);
        return "lesson/lesson_detail";
    }

    @GetMapping("/reservation/{id}")
    public String reservation(@PathVariable("id") Long id, Principal principal){
        Lesson lesson = lessonService.getLesson(id);
        SiteUser siteUser = siteUserService.getUser(principal.getName());

        lessonService.reservation(lesson, siteUser);

        return "redirect:/lesson/detail/" + id;
    }

    @GetMapping("/cancel/{id}")
    public String cancel(@PathVariable("id") Long id, Principal principal){
        Lesson lesson = lessonService.getLesson(id);
        SiteUser siteUser = siteUserService.getUser(principal.getName());

        lessonService.cancel(lesson, siteUser);
        return "redirect:/lesson/detail/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        Lesson lesson = lessonService.getLesson(id);
        lessonService.delete(lesson);
        return "redirect:/";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, LessonForm lessonForm){
        Lesson lesson = lessonService.getLesson(id);
        lessonForm.setLessonName(lesson.getLessonName());
        lessonForm.setHeadCount(String.valueOf(lesson.getHeadCount()));
        lessonForm.setLessonDate(lesson.getLessonDate());
        lessonForm.setStartTime(lesson.getStartTime());
        lessonForm.setEndTime(lesson.getEndTime());
        return "lesson/lesson_create";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, @Valid LessonForm lessonForm, BindingResult bindingResult){
        Lesson lesson = lessonService.getLesson(id);
        if(bindingResult.hasErrors()){
            return "lesson/lesson_create";
        }
        lessonService.update(lesson, lessonForm.getLessonName(), lessonForm.getHeadCount(), lessonForm.getLessonDate(), lessonForm.getStartTime(), lessonForm.getEndTime());
        return "redirect:/lesson/detail/" + id;
    }

    @GetMapping("/my-schedule")
    @ResponseBody
    public List<Lesson> getMySchedule() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SiteUser siteUser = siteUserService.findByUserName(username);
        List<Lesson> lessonList;
        if (siteUser.getUserRole().equals("user")){
            lessonList = lessonService.getLessonsAttendingByUsername(username);
        }else {
            lessonList = siteUserService.getLessonList(siteUser);
        }
        return lessonService.sortLessonsByDateAndTimeDesc(lessonList);
    }

}
