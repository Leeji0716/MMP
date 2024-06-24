package com.example.MMP.wod;

import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/wod")
public class WodController {
    private final WodService wodService;
    private final FileUploadUtil fileUploadUtil;
    private final SiteUserService siteUserService;

    @GetMapping("/form")
    private String wod(Model model){
        List<Wod> wodList = wodService.getList();
        // 현재 운영 체제에 따른 OSType 가져오기
        OSType osType = OSType.getInstance();
        // OSType에서 파일 저장 경로 가져오기
        String filePath = osType.getPath();

        model.addAttribute("wodList", wodList);
        model.addAttribute("filePath", filePath);

        return "wod/wod_form";
    }

    @GetMapping("/create")
    private String createWod(WodForm wodForm){
        return "wod/wod_create";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    private String createWod(@Valid WodForm wodForm, BindingResult bindingResult, @RequestParam("image") MultipartFile image, Model model, Principal principal){
        if (bindingResult.hasErrors()) {
            return "wod/wod_create";
        }

        SiteUser writer = siteUserService.getUser(principal.getName());
        // 이미지 파일의 경로가 비어있지 않으면 업로드를 시도합니다.
        if (image != null && !image.isEmpty()) {
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());

            try {
                wodForm.setImagePath(fileName);
                this.fileUploadUtil.saveFile(fileName, image);

            } catch (Exception e) {
                e.printStackTrace();
                bindingResult.reject("fileUploadError", "이미지 업로드 중 오류가 발생했습니다.");

                return "wod/wod_create"; // 업로드 실패 시 처리하는 방법에 따라 변경
            }
            wodService.create(fileName, wodForm.getContent(), writer);
        }

        List<Wod> wodList = wodService.getList();
        model.addAttribute("wodList", wodList);

        return "redirect:/wod/form";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        Wod wod = wodService.getWod(id);
        OSType osType = OSType.getInstance();
        String filePath = osType.getPath();
        model.addAttribute("filePath", filePath);
        model.addAttribute("wod", wod);
        model.addAttribute("commentList", wod.getCommentList());
        for (int a=0; a<wod.getLikeList().size();a++){
            System.out.println(wod.getLikeList().get(a).getUserId());
        }
        return "wod/wod_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, Principal principal){
        Wod wod = wodService.getWod(id);
        if (wod.getWriter().getUserId().equals(principal.getName())){
            wodService.delete(id);
        }
        return "redirect:/wod/form";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, WodForm wodForm,Model model){
        Wod wod = wodService.getWod(id);
        model.addAttribute("wod",wod);
        wodForm.setContent(wod.getContent());
        wodForm.setImagePath(wod.getImagePath());
        return "wod/wod_create";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/{id}")
    public String update(@Valid WodForm wodForm, BindingResult bindingResult, @PathVariable("id") Long id, Principal principal){
        if (bindingResult.hasErrors()) {
            return "wod/wod_create";
        }
        Wod wod = wodService.getWod(id);
        if (wod.getWriter().getUserId().equals(principal.getName())){
            wodService.update(id, wodForm.getContent());
        }
        return "redirect:/wod/detail/" + id;
    }

    @GetMapping("/like-status")
    public ResponseEntity<Map<String, Object>> getLikeStatus(@RequestParam Long wodId, Principal principal) {
        boolean liked = wodService.isLikedByUser(wodId, principal.getName());
        int likeCount = wodService.getLikeCount(wodId);

        Map<String, Object> response = new HashMap<>();
        response.put("liked", liked);
        response.put("likeCount", likeCount);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/like")
    public ResponseEntity<Map<String, Object>> likeWod(@RequestBody Map<String, Long> payload, Principal principal) {
        Long wodId = payload.get("wodId");
        Long likeCount = wodService.addLike(wodId, principal.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("likeCount", likeCount);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/like")
    public ResponseEntity<Map<String, Object>> unlikeWod(@RequestBody Map<String, Long> payload, Principal principal) {
        Long wodId = payload.get("wodId");
        Long likeCount = wodService.removeLike(wodId, principal.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("likeCount", likeCount);
        return ResponseEntity.ok(response);
    }
}
