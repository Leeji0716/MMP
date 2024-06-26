package com.example.MMP.Tag;



import com.example.MMP.challengeGroup.ChallengeGroup;
import com.example.MMP.challengeGroup.ChallengeGroupService;
import com.example.MMP.challengeGroup.GroupTag.GroupTag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    private final ChallengeGroupService challengeGroupService;

    @GetMapping("/detail/{groupId}")
    public String getGroupDetail(@PathVariable Long groupId, Model model) {
        ChallengeGroup group = challengeGroupService.getGroup(groupId); // 그룹 정보를 가져오는 서비스 메서드
        List<Tag> tags = tagService.getTagList(); // 태그 목록 가져오기
        model.addAttribute("group", group);
        model.addAttribute("tags", tags); // 태그 목록을 모델에 추가
        return "groupDetail";  // 뷰 템플릿 경로에 맞게 수정하세요.
    }

    @GetMapping("/{id}")
    public String getTag(@PathVariable Long id, Model model) {
        Tag tag = tagService.getTag(id);
        model.addAttribute("tag", tag);
        return "groupList_form";  // 뷰 템플릿 경로에 맞게 수정하세요.
    }

    @GetMapping("/create")
    public String createTagForm(Model model) {
        model.addAttribute("tag", new Tag());
        return "tags/new";  // 뷰 템플릿 경로에 맞게 수정하세요.
    }

    @PostMapping("/create")
    public String createTag(@ModelAttribute Tag tag) {
        tagService.create(tag.getName());
        return "redirect:/tags";
    }

    @GetMapping("/{id}/edit")
    public String editTagForm(@PathVariable Long id, Model model) {
        Tag tag = tagService.getTag(id);
        model.addAttribute("tag", tag);
        return "tags/edit";  // 뷰 템플릿 경로에 맞게 수정하세요.
    }

    @PostMapping("/{id}")
    public String updateTag(@PathVariable Long id, @ModelAttribute Tag tag) {
        Tag existingTag = tagService.getTag(id);
        existingTag.setName(tag.getName());
        tagService.create(existingTag.getName());
        return "redirect:/tags";
    }

    @PostMapping("/{id}/delete")
    public String deleteTag(@PathVariable Long id) {
        tagService.delete(id);
        return "redirect:/tags";
    }

    @GetMapping("/getTagName")
    public ResponseEntity<Map<String, String>> getTagName(@RequestParam Long id) {
        Tag tag = tagService.getTag(id); // 태그를 ID로 조회
        if (tag != null) {
            Map<String, String> response = new HashMap<>();
            response.put("name", tag.getName());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
