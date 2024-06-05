package com.example.MMP.wod;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/wod")
public class WodController {
    private final WodService wodService;
    private final FileUploadUtil fileUploadUtil;

    @GetMapping("/form")
    private String wod(Model model){
        List<Wod> wodList = wodService.getList();
        model.addAttribute("wodList", wodList);

        return "wod/wod_form";
    }

    @GetMapping("/create")
    private String createWod(){
        return "wod/wod_create";
    }

    @PostMapping("/create")
    private String createWod(@Valid WodForm wodForm, BindingResult bindingResult, @RequestParam("image") MultipartFile image, Model model){
        if (bindingResult.hasErrors()) {
            return "wod/wod_create";
        }

        // 이미지 파일의 경로가 비어있지 않으면 업로드를 시도합니다.
        if (image != null && !image.isEmpty()) {
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            String uploadDir = fileUploadUtil.getUploadDirPath();

            try {
                wodForm.setImagePath(fileName);
                this.fileUploadUtil.saveFile(uploadDir, fileName, image);

            } catch (Exception e) {
                e.printStackTrace();
                bindingResult.reject("fileUploadError", "이미지 업로드 중 오류가 발생했습니다.");

                return "wod/wod_create"; // 업로드 실패 시 처리하는 방법에 따라 변경
            }
            wodService.create(fileName, wodForm.getContent());
        }

        List<Wod> wodList = wodService.getList();
        model.addAttribute("wodList", wodList);

        return "redirect:/wod/form";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        Wod wod = wodService.getWod(id);
        model.addAttribute("wod", wod);
        model.addAttribute("commentList", wod.getCommentList());
        return "wod/wod_detail";
    }
}
