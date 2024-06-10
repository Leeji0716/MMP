package com.example.MMP.homeTraining;

import com.example.MMP.homeTraining.category.Category;
import com.example.MMP.homeTraining.category.CategoryService;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserService;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/homeTraining")
public class HomeTrainingController {
    private final HomeTrainingService homeTrainingService;
    private final SiteUserService siteUserService;
    private final CategoryService categoryService;

    @GetMapping("/home")
    public String main(Model model, @RequestParam(value = "categoryId", defaultValue = "0") int categoryId){
        List<HomeTraining> homeTrainingList = new ArrayList<>();

        List<Category> categoryList = categoryService.getList();

        for (Category category : categoryList){
            if (categoryId == 0){
                homeTrainingList.addAll(category.getHomeTrainingList());
//                homeTrainingList = homeTrainingService.getList();
            }else {
                if (categoryId == category.getId()){
                    homeTrainingList = homeTrainingService.getCategoryList(categoryId);
                    break;
                }
            }
        }
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("homeTrainingList", homeTrainingList);

        return "homeTraining/ht_main";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    private String create(HomeTrainingForm homeTrainingForm, Model model){
        List<Category> categoryList = this.categoryService.getList(); // 모든 카테고리를 가져옴
        model.addAttribute("categoryList", categoryList); // 카테고리 목록을 모델에 추가
        return "homeTraining/ht_create";
    }

    @PostMapping("/create")
    private String create(@Valid HomeTrainingForm homeTrainingForm, BindingResult bindingResult, Principal principal, Model model){
        List<Category> categoryList = this.categoryService.getList(); // 모든 카테고리를 가져옴
        model.addAttribute("categoryList", categoryList); // 카테고리 목록을 모델에 추가
        if(bindingResult.hasErrors()){
            return "homeTraining/ht_create";
        }
        SiteUser writer = siteUserService.getUser(principal.getName());

        Category category = this.categoryService.getCategory(homeTrainingForm.getCategoryID());

        if (category == null) {
            // 해당 ID에 해당하는 카테고리가 없는 경우
            bindingResult.rejectValue("categoryID", "category.error", "유효하지 않은 카테고리입니다.");
            return "product_form";
        }

        homeTrainingService.create(homeTrainingForm.getContent(), homeTrainingForm.getVideoUrl(), writer, category);

        return "redirect:/homeTraining/home";
    }

    @PostMapping("/bookmark")
    public ResponseEntity<Map<String, Object>> bookmarkHomeTraining(@RequestBody Map<String, Long> payload, Principal principal) {
        Long htId = payload.get("htId");
        homeTrainingService.addBookmark(htId, principal.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/bookmark")
    public ResponseEntity<Map<String, Object>> unbookmarkHomeTraining(@RequestBody Map<String, Long> payload, Principal principal) {
        Long htId = payload.get("htId");
        homeTrainingService.removeBookmark(htId, principal.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/bookmark/{htId}")
    public ResponseEntity<Map<String, Boolean>> toggleBookmark(@PathVariable Long htId, Principal principal) {
        SiteUser user = siteUserService.getUser(principal.getName());
        boolean bookmarkStatus = homeTrainingService.toggleBookmark(htId, user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("bookmarked", bookmarkStatus);
        if (bookmarkStatus) {
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.ok().body(response);
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        HomeTraining homeTraining = homeTrainingService.getHomeTraining(id);
        homeTrainingService.delete(homeTraining);
        return "redirect:/homeTraining/home";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, HomeTrainingForm homeTrainingForm, Model model){
        HomeTraining homeTraining = homeTrainingService.getHomeTraining(id);

        List<Category> categoryList = categoryService.getList();

        int categoryId = homeTraining.getCategory().getId();

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("categoryId", categoryId);

        homeTrainingForm.setContent(homeTraining.getContent());
        homeTrainingForm.setVideoUrl(homeTraining.getVideoUrl());
        homeTrainingForm.setCategoryID(categoryId);
        return "homeTraining/ht_create";
    }
}
