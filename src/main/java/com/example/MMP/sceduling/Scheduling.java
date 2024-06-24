package com.example.MMP.sceduling;

import com.example.MMP.lesson.Lesson;
import com.example.MMP.lesson.LessonService;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserService;
import com.example.MMP.userPass.UserDayPass;
import com.example.MMP.userPass.UserDayPassService;
import com.example.MMP.userPass.UserPtPass;
import com.example.MMP.userPass.UserPtPassService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class Scheduling {
    private final LessonService lessonService;
    private final SiteUserService siteUserService;
    private final UserPtPassService userPtPassService;
    private final UserDayPassService userDayPassService;

    @Scheduled(cron = "0 0 */1 * * *")
    @Transactional
    public void oneHourScheduled() {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + LocalDateTime.now() + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        LocalTime nowTime = LocalTime.now();
        List<Lesson> lessonList = lessonService.getLessonFromDate(LocalDate.now());
        for (Lesson lesson : lessonList) {
            if (nowTime.isAfter(lesson.getStartTime().minusHours(1)) && nowTime.isBefore(lesson.getStartTime())) {
                List<SiteUser> lessonUserList = lesson.getAttendanceList();
                Long i = 0L;
                for (SiteUser siteUser : lessonUserList) {
                    if (i >= lesson.getHeadCount()) {
                        break;
                    } else {
                        UserPtPass userPtPass = userPtPassService.findfinshTime(siteUser).get(0);
                        userPtPass.setPassCount((userPtPass.getPassCount() - 1));
                        if (userPtPass.getPassCount() == 0) {
                            userPtPassService.delete(userPtPass);
                        } else {
                            userPtPassService.save(userPtPass);
                        }
                        i++;
                    }
                }
            }
        }
    }


    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void oneDayPassChecking() {
        System.out.println("이용권" + LocalDateTime.now() + "삭제한다");
        LocalDate nowDate = LocalDate.now();
        List<UserPtPass> ptPassAll = userPtPassService.findAll();
        List<UserDayPass> dayPassAll = userDayPassService.findAll();
        for (UserPtPass userPtPass : ptPassAll) {
            if (nowDate.isEqual(userPtPass.getPassFinish())) {
                userPtPassService.delete(userPtPass);
            }
        }
        for (UserDayPass userDayPass : dayPassAll) {
            if (nowDate.isEqual(userDayPass.getPassFinish())) {
                userDayPassService.delete(userDayPass);
            }
        }
    }
}
