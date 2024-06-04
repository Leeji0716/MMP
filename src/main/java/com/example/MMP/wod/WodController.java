package com.example.MMP.wod;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/wod")
public class WodController {
    private WodService wodService;
    private final FileUploadUtil fileUploadUtil;

    @GetMapping("/form")
    private String wod(){
        return "wod/wod_form";
    }

    @GetMapping("/create")
    private String createWod(){
//        return "pass/ptpassmake";
        return "wod/wod_create";
    }

    @PostMapping("/create")
    private String createWod(@Valid WodForm wodForm, BindingResult bindingResult, @RequestParam("image") MultipartFile image){

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

            wodService.create(wodForm.getImagePath(), wodForm.getContent());
        }

        return "wod/wod_form";
    }
}
