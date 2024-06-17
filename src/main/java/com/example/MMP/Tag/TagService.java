package com.example.MMP.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public Tag getTag(Long tagId) {
        return tagRepository.findById(tagId).orElseThrow();
    }

    public Tag create(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        return tagRepository.save(tag);
    }

    public List<Tag> getTagList() {
        return tagRepository.findAll();
    }

    public Tag delete(Long tagId) {
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new IllegalArgumentException("Invalid tag Id: " + tagId));
        tagRepository.deleteById(tagId);
        return tag;
    }
}
