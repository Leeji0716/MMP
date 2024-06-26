package com.example.MMP.challengeGroup.GroupTag;

import com.example.MMP.Tag.Tag;
import com.example.MMP.Tag.TagService;
import com.example.MMP.challengeGroup.ChallengeGroup;
import com.example.MMP.challengeGroup.ChallengeGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupTagService {
    private final GroupTagRepository groupTagRepository;
    private final ChallengeGroupService groupService;
    private final TagService tagService;

    public GroupTag getGroupTag(Long groupTagId) {
        return groupTagRepository.findById(groupTagId).orElseThrow(() -> new IllegalArgumentException("Invalid Group Tag ID"));
    }

    public GroupTag create(Long groupId, String name) {
        ChallengeGroup group = groupService.getGroup(groupId);
        Tag tag = tagService.create(name);

        // 그룹에 해당 태그가 이미 존재하는지 확인
        for (GroupTag existingGroupTag : group.getGroupTagList()) {
            if (existingGroupTag.getTag().getName().equals(name)) {
                // 이미 존재하면 반환
                return existingGroupTag;
            }
        }

        // 존재하지 않으면 새로운 GroupTag 추가
        GroupTag groupTag = new GroupTag();
        groupTag.setGroup(group);
        groupTag.setTag(tag);

        return groupTagRepository.save(groupTag);
    }

    public void delete(GroupTag groupTag) {
        groupTagRepository.delete(groupTag);
    }
}



