package com.example.MMP.lesson;

import com.example.MMP.siteuser.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;

    public void create(String lessonName, String headCount, LocalDate lessonDate, LocalTime startTime, LocalTime endTime, SiteUser trainer) {
        Lesson lesson = new Lesson();
        lesson.setLessonName(lessonName);
        lesson.setHeadCount(Integer.parseInt(headCount));
        lesson.setLessonDate(lessonDate);
        lesson.setStartTime(startTime);
        lesson.setEndTime(endTime);
        lesson.setTrainer(trainer);

        lessonRepository.save(lesson);
    }

    public List<Lesson> getLessonFromDate(LocalDate localDate) {
        List<Lesson> lessonList = lessonRepository.findByLessonDate(localDate);
        return lessonList;
    }
}
