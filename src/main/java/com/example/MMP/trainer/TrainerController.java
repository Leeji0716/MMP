package com.example.MMP.trainer;

import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserService;
import com.example.MMP.wod.FileUploadUtil;
import com.example.MMP.wod.OSType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
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
    private final SiteUserService siteUserService;
    private final FileUploadUtil fileUploadUtil;

    @GetMapping("/form")
    private String trainer(Model model) {
        List<Trainer> trainerList = trainerService.getList();
        model.addAttribute("trainerList", trainerList);
        return "trainer/trainer_form";
    }

    @GetMapping("/create")
    private String createTrainer(TrainerForm trainerForm, Model model) {
        List<SiteUser> userTrainerList = siteUserService.getTrainerList();
        model.addAttribute("userTrainerList", userTrainerList);
        return "trainer/trainer_create";
    }

    @PostMapping("/create")
    private String createTrainer(@Valid TrainerForm trainerForm,
                                 BindingResult bindingResult,
                                 @RequestParam("image") MultipartFile image,
                                 Model model) {

        if (bindingResult.hasErrors()) {
            List<SiteUser> userTrainerList = siteUserService.getTrainerList();
            model.addAttribute("userTrainerList", userTrainerList);
            return "trainer/trainer_create";
        }
        // 사용자 정보 가져오기
        SiteUser userTrainer = siteUserService.findById(trainerForm.getUserTrainerId());

        try {
            // 이미지 업로드 처리
            if (image != null && !image.isEmpty()) {
                String fileName = StringUtils.cleanPath(image.getOriginalFilename());

                try {
                    this.fileUploadUtil.saveFile(fileName, image);
                } catch (Exception e) {
                    e.printStackTrace();
                    bindingResult.reject("fileUploadError", "이미지 업로드 중 오류가 발생했습니다.");
                    return "trainer/trainer_create";
                }

                trainerService.create(fileName, userTrainer, trainerForm.getIntroduce(),
                        userTrainer.getGender(), trainerForm.getClassType(), trainerForm.getSpecialization());
            }

            // 트레이너 목록 조회 및 모델에 추가
            List<Trainer> trainerList = trainerService.getList();
            model.addAttribute("trainerList", trainerList);

            // 리다이렉트
            return "redirect:/trainer/list";

        } catch (DataIntegrityViolationException e) {
            // 이미 등록된 트레이너를 등록하려고 시도할 경우 예외 처리
            bindingResult.reject("duplicateTrainer", "이미 등록된 트레이너입니다.");
            List<SiteUser> userTrainerList = siteUserService.getTrainerList();
            model.addAttribute("userTrainerList", userTrainerList);
            return "trainer/trainer_create";
        }
    }

    @GetMapping("/checkDuplicate/{trainerId}")
    @ResponseBody
    public Trainer checkDuplicateTrainer(@PathVariable Long trainerId) {
        List<Trainer> trainerList = trainerService.getList();
        SiteUser userTrainer = siteUserService.findById(trainerId);
        Trainer duplicateTrainer = null;

        for (Trainer trainer : trainerList) {
            SiteUser trainerUser = trainer.getUserTrainer();

            // Null 체크와 userTrainer의 id 비교로 중복 확인
            if (trainerUser != null && userTrainer == trainer.getUserTrainer()) {
                duplicateTrainer = trainer;
                break;
            }
        }
        return duplicateTrainer; // 이미 등록된 트레이너인 경우 해당 트레이너 객체 반환
    }

    @GetMapping("/list")
    public String TrainerList(Model model) {
        List<Trainer> trainerList = trainerService.findAll();
        OSType osType = OSType.getInstance();
        String filePath = osType.getPath();
        model.addAttribute("filePath", filePath);
        model.addAttribute("trainerList", trainerList);
        return "trainer/trainer_list";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        Trainer trainer = trainerService.findById(id);
        trainerService.delete(trainer);
        return "redirect:/trainer/list";
    }
    @PostMapping("/update/{id}")
    public String update(@Valid TrainerForm trainerForm,
                         BindingResult bindingResult,
                         @PathVariable("id") Long id, @RequestParam("image") MultipartFile image) {
        if (bindingResult.hasErrors()) {
            return "trainer/trainer_create";
        }
        Trainer trainer = trainerService.getTrainer(id);
        String fileName;
        // 이미지 업로드 처리
        if (image != null && !image.isEmpty()) {
            fileName = StringUtils.cleanPath(image.getOriginalFilename());

            try {
                this.fileUploadUtil.saveFile(fileName, image);
            } catch (Exception e) {
                e.printStackTrace();
                bindingResult.reject("fileUploadError", "이미지 업로드 중 오류가 발생했습니다.");
                return "trainer/trainer_create";
            }
            
        }else {
            fileName = trainer.getImagePath();
        }
        trainerService.update(trainer, trainerForm.getIntroduce(), trainerForm.getClassType(), trainerForm.getSpecialization(), fileName);
        return "redirect:/trainer/list";
    }

    @PostMapping("/filter")
    public ResponseEntity<List<Trainer>> filterTrainers(@RequestBody FilterRequest filterRequest) {
        List<Trainer> trainerList = trainerService.findAll();
        List<Trainer> filteredTrainers;
        filteredTrainers = trainerService.filterTrainers(trainerList, filterRequest);

        return ResponseEntity.ok(filteredTrainers);
    }
}