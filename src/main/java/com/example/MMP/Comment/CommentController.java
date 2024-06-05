package com.example.MMP.Comment;

import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserService;
import com.example.MMP.wod.Wod;
import com.example.MMP.wod.WodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping("/create/{wodId}")
    public String createAnswer(Model model, @PathVariable("wodId") Long wodId,
                               @Valid CommentForm commentForm, BindingResult bindingResult) {
        Wod wod = this.wodService.getWod(wodId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("wod", wod);
            return String.format("redirect:/wod/detail/%s", wodId);
        }

        Comment comment = this.commentService.create(wod, commentForm.getContent());
        return String.format("redirect:/wod/detail/%s#comment_%s", comment.getWod().getId(), comment.getId());
    }

}
