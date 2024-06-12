package com.example.MMP.lesson;

import com.example.MMP.siteuser.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;

    public void create(String lessonName, String headCount, LocalDateTime startDateTime, LocalDateTime endDateTime, SiteUser trainer) {
        Lesson lesson = new Lesson();
        lesson.setLessonName(lessonName);
        lesson.setHeadCount(Integer.parseInt(headCount));
        lesson.setStartDateTime(startDateTime);
        lesson.setEndDateTime(endDateTime);
        lesson.setTrainer(trainer);

        lessonRepository.save(lesson);
    }
}
