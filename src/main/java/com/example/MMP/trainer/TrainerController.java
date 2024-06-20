package com.example.MMP.trainer;

import com.example.MMP.wod.FileUploadUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    private final FileUploadUtil fileUploadUtil;

    @GetMapping("/form")
    private String trainer(Model model) {
        List<Trainer> trainerList = trainerService.getList();
        model.addAttribute("trainerList", trainerList);
        return "trainer/trainer_form";
    }

    @GetMapping("/category")
    private String category() {
        return "trainer/categoryFilter";
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

        if (image != null && !image.isEmpty()) {
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());

            try {
                this.fileUploadUtil.saveFile(fileName, image);
            } catch (Exception e) {
                e.printStackTrace();
                bindingResult.reject("fileUploadError", "이미지 업로드 중 오류가 발생했습니다.");
                return "trainer/trainer_create";
            }

            trainerService.create(fileName, trainerForm.getTrainerName(), trainerForm.getIntroduce(),
                    trainerForm.getGender(), trainerForm.getClassType(), trainerForm.getSpecialization());
        }

        List<Trainer> trainerList = trainerService.getList();
        model.addAttribute("trainerList", trainerList);

        return "redirect:/";
    }

    @GetMapping("/list")
    public String TrainerList(Model model) {
        List<Trainer> trainerList = trainerService.findAll();
        model.addAttribute("trainerList", trainerList);
        return "trainer/trainer_list";
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

//    @PostMapping("/filter")
//    public String filterTrainers(@RequestParam(name = "gender", required = false) String gender,
//                                 @RequestParam(name = "classType", required = false) String classType,
//                                 @RequestParam(name = "specialization", required = false) String specialization,
//                                 Model model) {
//        List<Trainer> filteredTrainers = trainerService.filterTrainers(gender, classType, specialization);
//        model.addAttribute("trainerList", filteredTrainers);
//        return "trainer/trainer_list";
//    }

    @PostMapping("/filter")
    public ResponseEntity<List<Trainer>> filterTrainers(@RequestBody FilterRequest filterRequest) {
        List<Trainer> filteredTrainers = trainerService.filterTrainers(filterRequest);
        System.out.println(filterRequest.getGender());
        System.out.println(filterRequest.getClassType());
        System.out.println(filterRequest.getSpecialization());
        return ResponseEntity.ok(filteredTrainers);
    }
}