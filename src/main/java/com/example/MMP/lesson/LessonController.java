package com.example.MMP.lesson;

import com.example.MMP.homeTraining.HomeTrainingForm;
import com.example.MMP.homeTraining.category.Category;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserService;
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
        LocalDateTime startDateTime = LocalDateTime.of(lessonForm.getLessonDate(), lessonForm.getStartTime());
        LocalDateTime endDateTime = LocalDateTime.of(lessonForm.getLessonDate(), lessonForm.getEndTime());

        if(bindingResult.hasErrors()){
            return "lesson/lesson_create";
        }
        SiteUser trainer = siteUserService.getUser(principal.getName());

        lessonService.create(lessonForm.getLessonName(), lessonForm.getHeadCount(), startDateTime, endDateTime, trainer);

        return "redirect:/schedule";
    }

}
