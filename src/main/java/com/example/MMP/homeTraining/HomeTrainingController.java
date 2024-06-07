package com.example.MMP.homeTraining;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/homeTraining")
public class HomeTrainingController {
    private final HomeTrainingService homeTrainingService;
    private final SiteUserService siteUserService;

    @GetMapping("/home")
    public String main(Model model){
        List<HomeTraining> homeTrainingList = homeTrainingService.getList();
        model.addAttribute("homeTrainingList", homeTrainingList);

        return "homeTraining/ht_main";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    private String create(HomeTrainingForm homeTrainingForm){
        return "homeTraining/ht_create";
    }

    @PostMapping("/create")
    private String create(@Valid HomeTrainingForm homeTrainingForm, BindingResult bindingResult, Principal principal){
        if(bindingResult.hasErrors()){
            return "homeTraining/ht_create";
        }
        SiteUser writer = siteUserService.getUser(principal.getName());
        homeTrainingService.create(homeTrainingForm.getContent(), homeTrainingForm.getVideoUrl(), writer);

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
}
