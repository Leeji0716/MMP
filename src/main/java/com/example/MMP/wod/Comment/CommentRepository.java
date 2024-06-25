package com.example.MMP.wod.Comment;

import com.example.MMP.wod.Wod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByWodOrderByCreateDateDesc(Wod wod);
}
