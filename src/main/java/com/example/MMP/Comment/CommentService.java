package com.example.MMP.Comment;

import com.example.MMP.DataNotFoundException;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.wod.Wod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    public Comment create(Wod wod, String content, SiteUser writer){
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setWod(wod);
        comment.setWriter(writer);
        this.commentRepository.save(comment);
        return comment;
    }

    public Comment getComment(Long id) {
        Optional<Comment> comment = this.commentRepository.findById(id);
        if (comment.isPresent()) {
            return comment.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public void modify(Comment comment, String content) {
        comment.setContent(content);
        comment.setModifyDate(LocalDateTime.now());
        this.commentRepository.save(comment);
    }

    public void delete(Long id) {
        Comment comment = commentRepository.findById(id).get();
        this.commentRepository.delete(comment);
    }

    public void update(Comment comment, String content) {
        comment.setContent(content);
        this.commentRepository.save(comment);
    }

    public List<Comment> getCommentsByWodOrderByCreateDateDesc(Wod wod) {
        return commentRepository.findByWodOrderByCreateDateDesc(wod);
    }

    public List<Comment> sortCommentsByCreateDateDesc(List<Comment> comments) {
        return comments.stream()
                .sorted((c1, c2) -> c2.getCreateDate().compareTo(c1.getCreateDate()))
                .collect(Collectors.toList());
    }

    public List<Comment> getTop7Comments(List<Comment> comments) {
        List<Comment> sortedComments = sortCommentsByCreateDateDesc(comments);
        return sortedComments.stream()
                .limit(7)
                .collect(Collectors.toList());
    }
}
