package com.example.MMP.challengeGroup.GroupTag;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/groupChallenge/{groupId}/tags")
public class GroupTagController {
    private final GroupTagService groupTagService;

    @PostMapping("/create")
    public String create(@PathVariable("groupId") Long groupId, String name) {
        GroupTag groupTag = groupTagService.create(groupId, name);

        return "redirect:/groupChallenge/detail/" + groupId;
    }

    @GetMapping("/{groupTagId}/delete")
    public String delete(@PathVariable("groupId") Long groupId, @PathVariable("groupTagId") Long groupTagId) {
        GroupTag groupTag = groupTagService.getGroupTag(groupTagId);
        groupTagService.delete(groupTag);

        return "redirect:/groupChallenge/detail/" + groupId;
    }
}
