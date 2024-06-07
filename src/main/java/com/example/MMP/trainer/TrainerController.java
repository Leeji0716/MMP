package com.example.MMP.trainer;

import com.example.MMP.wod.FileUploadUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/trainer")
public class TrainerController {
    private final TrainerService trainerService;
    private final FileUploadUtil fileUploadUtil;

    @GetMapping("/form")
    private String trainer(Model model) {
        List<Trainer> trainerList = trainerService.getList();
        model.addAttribute("trainerList", trainerList);

        return "trainer/trainer_form";
    }

    @GetMapping("/create")
    private String createTrainer(TrainerForm trainerForm) {
        return "trainer/trainer_create";
    }

    @PostMapping("/create")
    private String createTrainer(@Valid TrainerForm trainerForm,
                                 BindingResult bindingResult,
                                 @RequestParam("image") MultipartFile image,
                                 Model model) {

        if (bindingResult.hasErrors()) {
            return "trainer/trainer_create";
        }

        // 이미지 파일의 경로가 비어있지 않으면 업로드를 시도합니다.
        if (image != null && !image.isEmpty()) {
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            String uploadDir = fileUploadUtil.getUploadDirPath();

            try {
                this.fileUploadUtil.saveFile(uploadDir, fileName, image);

            } catch (Exception e) {
                e.printStackTrace();
                bindingResult.reject("fileUploadError", "이미지 업로드 중 오류가 발생했습니다.");

                return "trainer/trainer_create"; // 업로드 실패 시 처리하는 방법에 따라 변경
            }
            trainerService.create(fileName,trainerForm.getTrainerName(), trainerForm.getIntroduce());
        }

        List<Trainer> trainerList = trainerService.getList();
        model.addAttribute("trainerList", trainerList);

        return "redirect:/";
    }

    @GetMapping("/list")
    public String TrainerList(Model model) {
        List<Trainer> trainerList = trainerService.findAll();
        model.addAttribute("trainerList", trainerList);
        return "trainer/trainerList";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id,
                         Model model) {

        Trainer trainer = trainerService.getTrainer(id);
        model.addAttribute("trainer", trainer);
        return "trainer/trainer_detail";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        trainerService.delete(id);
        return "redirect:/trainer/form";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Long id,
                         TrainerForm trainerForm,
                         Model model) {

        Trainer trainer = trainerService.getTrainer(id);
        model.addAttribute("trainer", trainer);
        trainerForm.setIntroduce(trainer.getIntroduce());
        return "trainer/trainer_create";
    }

    @PostMapping("/update/{id}")
    public String update(@Valid TrainerForm trainerForm,
                         BindingResult bindingResult,
                         @PathVariable("id") Long id) {

        if (bindingResult.hasErrors()) {
            return "trainer/trainer_create";
        }
        trainerService.update(id, trainerForm.getIntroduce());
        return "redirect:/trainer/detail/" + id;
    }
}