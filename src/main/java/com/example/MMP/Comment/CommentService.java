package com.example.MMP.Comment;

import com.example.MMP.Comment.CommentRepository;
import com.example.MMP.DataNotFoundException;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.wod.Wod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    public Comment create(Wod wod, String content){
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setWod(wod);
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

    public void delete(Comment comment) {
        this.commentRepository.delete(comment);
    }

//    public void vote(Comment comment, SiteUser siteUser){
//        comment.getLikeList().add(siteUser);
//        int vote = comment.getLikeList().size();
//        comment.setLike(vote);
//        this.commentRepository.save(comment);
//    }
}
