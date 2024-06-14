package com.example.MMP.lesson;

import com.example.MMP.homeTraining.HomeTrainingForm;
import com.example.MMP.homeTraining.category.Category;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserService;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
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

        return "redirect:/schedule";
    }

    @PostMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model, Principal principal){
        Lesson lesson = lessonService.getLesson(id);
        String currentUsername = principal.getName();


        boolean isUserAttending = lesson.getAttendanceList().stream()
                .anyMatch(attendant -> attendant.getName().equals(currentUsername));

        System.out.println(lesson.getAttendanceList().size());
        for(SiteUser siteUser : lesson.getAttendanceList()){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + siteUser.getName());
        }

        model.addAttribute("lesson", lesson);
        model.addAttribute("isUserAttending", isUserAttending);
        return "lesson/lesson_detail";
    }

    @GetMapping("/reservation/{id}")
    public String reservation(@PathVariable("id") Long id, Principal principal){
        Lesson lesson = lessonService.getLesson(id);
        SiteUser siteUser = siteUserService.getUser(principal.getName());

        lessonService.reservation(lesson, siteUser);
//        return "redirect:/lesson/detail/" + id;
        return "redirect:/schedule";
    }

//    @PostMapping("/{lessonId}/attend")
//    public ResponseEntity<String> attendLesson(@PathVariable Long lessonId, Authentication authentication) {
//        // Lesson ID를 이용해 해당 Lesson 객체를 가져옴
//        Lesson lesson = lessonService.getLessonById(lessonId);
//
//        if (lesson == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found");
//        }
//
//        // 현재 인증된 사용자 정보 가져오기 (Principal 정보)
//        String username = authentication.getName();
//        SiteUser currentUser = siteUserService.findByUserId(username);
//
//        if (currentUser == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
//        }
//
//        // Lesson에 현재 사용자를 추가
//        lesson.addAttendee(currentUser);
//        lessonService.saveLesson(lesson); // 변경 사항 저장
//
//        // 변경된 Lesson 정보를 클라이언트에 반환
//        return ResponseEntity.ok("Successfully attended lesson");
//    }

}
