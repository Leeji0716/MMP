package com.example.MMP.wod;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WodService {
    private WodRepository wodRepository;
    public void create(String imagePath, String content) {
        Wod wod = new Wod();
        wod.setImagePath(imagePath);
        wod.setContent(content);
        wod.setCreateDate(LocalDateTime.now());

        wodRepository.save(wod);
    }
}
