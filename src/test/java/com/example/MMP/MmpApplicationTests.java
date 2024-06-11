package com.example.MMP;

import com.example.MMP.notice.NoticeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MmpApplicationTests {
    @Autowired
    private NoticeService noticeService;

    @Test
    void testJpa() {
        for (int i = 1; i <= 30; i++) {
            String title = String.format("테스트 데이터입니다");
            String content = "내용무";
            this.noticeService.create(title, content);
        }
    }
}
