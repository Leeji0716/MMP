package com.example.MMP.notice;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @Getter
    @Setter
    public class NoticeForm {
        @NotNull(message = "제목은 필수로 채워주세요")
        private String title;

        @NotNull(message = "내용은 필수로 채워주세요")
        private String content;
    }

    @GetMapping("/create")
    public String create(NoticeForm noticeForm){
        return "noticeCreate_form";
    }

    @PostMapping("/create")
    public String create(@Valid NoticeForm noticeForm, BindingResult bindingResult){
        noticeService.create (noticeForm.title,noticeForm.content);
        return "redirect:/";
    }


}
