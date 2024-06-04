package com.example.MMP.notice;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Notice create(String title, String content) {
        Notice notice = new Notice ();

        notice.setTitle (title);
        notice.setContent (content);
        notice.setNotificationDate (LocalDateTime.now ());
        notice.setHit (0);

        return noticeRepository.save(notice);
    }
}
