package com.example.MMP.information;

import com.example.MMP.wod.FileUploadUtil;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/information")
public class InformationController {
    private final InformationService informationService;
    private final FileUploadUtil fileUploadUtil;

    @GetMapping("/update")
    public String update(InformationForm informationForm) {
        Information information = informationService.getInformation();
        if (information != null){
            informationForm.setImagePath(information.getImagePath());
            informationForm.setHealthName(information.getHealthName());
            informationForm.setCompanyNumber(information.getCompanyNumber());
            informationForm.setAddress(information.getAddress());
            informationForm.setCallNumber(information.getCallNumber());
            informationForm.setEmail(information.getEmail());
            informationForm.setText(information.getText());
        }
        return "information_form";
    }

    @PostMapping("/update")
    public String update(@Valid InformationForm informationForm, BindingResult bindingResult,  @RequestParam("image") MultipartFile image){
        if (bindingResult.hasErrors()){
            return "information_form";
        }
        Information information = informationService.getInformation();

        // 이미지 파일의 경로가 비어있지 않으면 업로드를 시도합니다.
        if (image != null && !image.isEmpty()) {
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());

            try {
                informationForm.setImagePath(fileName);
                this.fileUploadUtil.saveFile(fileName, image);

            } catch (Exception e) {
                e.printStackTrace();
                bindingResult.reject("fileUploadError", "이미지 업로드 중 오류가 발생했습니다.");

                return "information_form"; // 업로드 실패 시 처리하는 방법에 따라 변경
            }
            informationService.update(informationForm.getImagePath(), informationForm.getHealthName(), informationForm.getCompanyNumber(),
                    informationForm.getAddress(), informationForm.getCallNumber(), informationForm.getEmail(), informationForm.getText());
        }

        return "redirect:/";
    }
}
