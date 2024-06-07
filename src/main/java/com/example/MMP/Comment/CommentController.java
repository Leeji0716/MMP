package com.example.MMP.Comment;

import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserService;
import com.example.MMP.wod.Wod;
import com.example.MMP.wod.WodService;
import jakarta.persistence.Version;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {
    private final WodService wodService;
    private final CommentService commentService;
    private final SiteUserService siteUserService;

    @GetMapping("/create")
    public String createComment(CommentForm commentForm) {
        return "comment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{wodId}")
    public String createAnswer(Model model, @PathVariable("wodId") Long wodId,
                               @Valid CommentForm commentForm, BindingResult bindingResult, Principal principal) {
        Wod wod = this.wodService.getWod(wodId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("wod", wod);
            return String.format("redirect:/wod/detail/%s", wodId);
        }
        SiteUser writer = siteUserService.getUser(principal.getName());

        Comment comment = this.commentService.create(wod, commentForm.getContent(), writer);
        return String.format("redirect:/wod/detail/%s#comment_%s", comment.getWod().getId(), comment.getId());
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        Comment comment = commentService.getComment(id);
        Wod wod = comment.getWod();
        commentService.delete(id);
        return "redirect:/wod/detail/" + wod.getId();
    }

    @GetMapping("/update/{id}")
    public String delete(@PathVariable("id") Long id, CommentForm commentForm){
        Comment comment = commentService.getComment(id);
        commentForm.setContent(comment.getContent());
        return "comment_form";
    }

    @PostMapping("/update/{id}")
    public String delete(@PathVariable("id") Long id, @Valid CommentForm commentForm, BindingResult bindingResult){
        Comment comment = commentService.getComment(id);
        if(bindingResult.hasErrors()){
            return "comment_form";
        }

        Wod wod = comment.getWod();
        commentService.update(comment, commentForm.getContent());;
        return "redirect:/wod/detail/" + wod.getId();
    }

}
