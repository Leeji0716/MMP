package com.example.MMP.homeTraining;

import com.example.MMP.siteuser.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeTrainingService {
    private final HomeTrainingRepository homeTrainingRepository;

    public void create(String content, String videoUrl, SiteUser writer) {
        HomeTraining homeTraining = new HomeTraining();
        homeTraining.setContent(content);
        homeTraining.setVideoUrl(videoUrl);
        homeTraining.setCreateDate(LocalDateTime.now());
        homeTraining.setWriter(writer);

        String videoId = videoUrl.split("v=")[1];
        String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/0.jpg";
        homeTraining.setThumbnailUrl(thumbnailUrl);
        homeTrainingRepository.save(homeTraining);
    }

    public List<HomeTraining> getList() {
        List<HomeTraining> homeTrainingList = homeTrainingRepository.findAll();
        return homeTrainingList;
    }
}
