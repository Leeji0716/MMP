package com.example.MMP.lesson;

import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserRepository;
import com.example.MMP.siteuser.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final SiteUserRepository siteUserRepository;

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

    public Lesson getLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id).get();
        return lesson;
    }

    @Transactional
    public void reservation(Lesson lesson, SiteUser siteUser) {
        lesson.getAttendanceList().add(siteUser);
        siteUser.getLessonsAttending().add(lesson);
    }

    @Transactional
    public void cancel(Lesson lesson, SiteUser siteUser) {
        lesson.getAttendanceList().remove(siteUser);
        siteUser.getLessonsAttending().remove(lesson);
    }

    public void delete(Lesson lesson) {
        for (SiteUser user : lesson.getAttendanceList()){
            user.getLessonsAttending().remove(lesson);
        }
        lesson.getAttendanceList().clear();
        lessonRepository.delete(lesson);
    }

    public void update(Lesson lesson, String lessonName, String headCount, LocalDate lessonDate, LocalTime startTime, LocalTime endTime) {
        lesson.setLessonName(lessonName);
        lesson.setHeadCount(Integer.parseInt(headCount));
        lesson.setLessonDate(lessonDate);
        lesson.setStartTime(startTime);
        lesson.setEndTime(endTime);

        lessonRepository.save(lesson);
    }

    public List<Lesson> getLessonsAttendingByUsername(String username) {
        SiteUser siteUser = siteUserRepository.findByUserId(username).get();
        List<Lesson> lessonList = siteUser.getLessonsAttending();
        return lessonList;
    }

    public List<Lesson> sortLessonsByDateAndTimeDesc(List<Lesson> lessonList) {
        // lessonDate와 startTime을 기준으로 내림차순 정렬하는 Comparator 생성
        Comparator<Lesson> byDateAndTimeDesc = (lesson1, lesson2) -> {
            LocalDateTime dateTime1 = LocalDateTime.of(lesson1.getLessonDate(), lesson1.getStartTime());
            LocalDateTime dateTime2 = LocalDateTime.of(lesson2.getLessonDate(), lesson2.getStartTime());
            return dateTime2.compareTo(dateTime1); // 내림차순 정렬
        };

        // lessonList를 lessonDate와 startTime 기준으로 정렬
        Collections.sort(lessonList, byDateAndTimeDesc);

        return lessonList;
    }

}
