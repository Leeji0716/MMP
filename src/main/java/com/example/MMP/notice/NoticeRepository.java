package com.example.MMP.notice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Page<Notice> findAll(Pageable pageable);

    Optional<Notice> findById(Integer id);

    Notice findByTitle(String subject);

    Notice findByTitleAndContent(String subject, String content);
}
