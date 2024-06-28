package com.example.MMP.notice;

import com.example.MMP.wod.FileUploadUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;
    private final FileUploadUtil fileUploadUtil;

    @Getter
    @Setter
    public class NoticeForm {
        @NotNull(message = "제목은 필수로 채워주세요")
        private String title;

        @NotNull(message = "내용은 필수로 채워주세요")
        private String content;
    }

    @GetMapping("/create")
    public String create(NoticeForm noticeForm) {
        return "notice/noticeCreate_form";
    }

    @PostMapping("/create")
    public String create(@Valid NoticeForm noticeForm,
                         BindingResult bindingResult) {
        

        noticeService.create(noticeForm.title, noticeForm.content);
        return "redirect:/notice/list";
    }

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page) {

        Page<Notice> paging = this.noticeService.getList(page);
        model.addAttribute("paging", paging);
        return "notice/notice";
    }

    @RequestMapping(value = "/detail/{id}")
    public String detail(Model model,
                         HttpSession session,
                         @RequestParam(value = "so", defaultValue = "recent") String so,
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         @PathVariable("id") Integer id,
                         Principal principal) {

        Notice notice = this.noticeService.getNotice(id);
        // Notice의 Content를 HTML로 전환
        String markdownContent = notice.getContent();
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdownContent);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String htmlContent = renderer.render(document);

        // 현재 사용자의 세션에서 조회한 공지사항 ID 목록을 가져옵니다.
        Set<Integer> viewedNotices = (Set<Integer>) session.getAttribute("viewedNotices");

        // 만약 조회한 공지사항 ID 목록이 없으면 빈 목록을 생성합니다.
        if (viewedNotices == null) {
            viewedNotices = new HashSet<>();
        }

        // 만약 사용자가 해당 공지사항을 처음 조회하는 경우에만 조회수를 증가시킵니다.
        if (!viewedNotices.contains(id)) {
            notice.increaseHit(); // 조회수 증가
            viewedNotices.add(id); // 조회한 공지사항 ID 목록에 추가
            session.setAttribute("viewedNotices", viewedNotices); // 세션에 조회한 공지사항 ID 목록 저장
            this.noticeService.saveNotice(notice); // 변경된 조회수를 데이터베이스에 저장
        }

        Page<Notice> paging = this.noticeService.getList(page);

        // Model 객체에 조회된 질문, 답변 목록, 정렬 옵션을 담아서 뷰로 전달합니다.
        model.addAttribute("htmlContent", htmlContent);
        model.addAttribute("paging", paging);
        model.addAttribute("notice", notice);
        model.addAttribute("so", so);

        // 뷰 이름인 "notice_detail"을 반환합니다.
        return "notice/notice_detail";
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            fileUploadUtil.saveFile(fileName, file);

            String fileDownloadUri = "/images/" + fileName;
            Map<String, Object> response = new HashMap<>();
            response.put("url", fileDownloadUri);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "파일 업로드 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id,NoticeForm noticeForm,Model model){
        Notice notice = noticeService.getNotice(id);
        noticeForm.setTitle(notice.getTitle());
        noticeForm.setContent(notice.getContent());

        return "notice/noticeCreate_form";

    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id,@Valid NoticeForm noticeForm,BindingResult bindingResult){
        Notice notice = noticeService.getNotice(id);
        notice.setContent(noticeForm.getContent());
        notice.setTitle(noticeForm.getTitle());
        noticeService.saveNotice(notice);

        return "redirect:/notice/list";

    }
}
