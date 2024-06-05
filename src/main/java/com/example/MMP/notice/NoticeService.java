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
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.noticeRepository.findAll(pageable);
    }

    public Notice getNotice(Integer id) {
        Optional<Notice> notice = this.noticeRepository.findById(id);

        if (notice.isPresent()) {
            return notice.get();
        } else {
            throw new DataNotFoundException("notice Not Found");
        }
    }

    public void incrementView(Notice notice) {
        notice.setHit(notice.getHit() + 1);
        this.noticeRepository.save(notice);
    }

    public Page<Notice> getList(int page, Notice notice, String so) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        // 페이징을 위한 페이지 요청 객체를 생성합니다.
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.noticeRepository.findAll(pageable);
    }
}
