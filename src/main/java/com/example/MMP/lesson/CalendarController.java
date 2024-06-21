package com.example.MMP.lesson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lessonCalendar")
public class CalendarController {
    private final LessonService lessonService;

    @PostMapping("/selected-date")
    @ResponseBody
    public Map<String, Object> handleSelectedDate(@RequestBody Map<String, String> payload) {
        String selectedDate = payload.get("date");

        // String을 LocalDate로 변환
        LocalDate selectedLocalDate = LocalDate.parse(selectedDate);

        List<Lesson> lessonList = lessonService.getLessonFromDate(selectedLocalDate);

        // JSON 형식으로 반환할 Map 생성
        Map<String, Object> response = new HashMap<>();
        response.put("lessons", lessonList); // lessons라는 키에 lessonList를 할당

        // JSON 응답을 출력
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.registerModule(new JavaTimeModule());
//            String jsonResponse = mapper.writeValueAsString(response);
//            System.out.println("JSON Response: " + jsonResponse);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return response;
    }
}
