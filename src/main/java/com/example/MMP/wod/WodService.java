package com.example.MMP.wod;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WodService {
    private final WodRepository wodRepository;

    public void create(String imagePath, String content) {
        Wod wod = new Wod();
        wod.setImagePath(imagePath);
        wod.setContent(content);
        wod.setCreateDate(LocalDateTime.now());

        this.wodRepository.save(wod);
    }

    public List<Wod> getList() {
        List<Wod> wodList = wodRepository.findAll();
        return wodList;
    }
}
