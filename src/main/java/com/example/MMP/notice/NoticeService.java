package com.example.MMP.notice;

import com.example.MMP.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Page<Notice> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("notificationDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.noticeRepository.findAll(pageable);
    }

    public Notice getNotice(Integer id) {
        Optional<Notice> notice = this.noticeRepository.findById(id);
        if (notice.isPresent()) {
            return notice.get();
        } else {
            throw new DataNotFoundException("notice not found");
        }
    }

    public void incrementHit(Notice notice) {
        notice.setHit(notice.getHit() + 1);
        this.noticeRepository.save(notice);
    }
}
