package com.example.MMP.homeTraining;

import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserRepository;
import com.example.MMP.siteuser.SiteUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HomeTrainingService {
    private final HomeTrainingRepository homeTrainingRepository;
    private final SiteUserRepository siteUserRepository;

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

    public int addBookmark(Long htId, String username) {
        SiteUser user = siteUserRepository.findByUserId(username).orElseThrow(() -> new EntityNotFoundException("user not found"));
        HomeTraining homeTraining = homeTrainingRepository.findById(htId).orElseThrow(() -> new EntityNotFoundException("HomeTraining not found"));

        user.getSaveTraining().add(homeTraining);
        siteUserRepository.save(user);

        return user.getSaveTraining().size();
    }

    public int removeBookmark(Long htId, String username) {
        SiteUser user = siteUserRepository.findByUserId(username).orElseThrow(() -> new EntityNotFoundException("user not found"));
        HomeTraining homeTraining = homeTrainingRepository.findById(htId).orElseThrow(() -> new EntityNotFoundException("HomeTraining not found"));

        user.getSaveTraining().remove(homeTraining);
        siteUserRepository.save(user);

        return user.getSaveTraining().size();
    }

    public boolean toggleBookmark(Long htId, SiteUser user) {
        Set<HomeTraining> homeTrainingSet = user.getSaveTraining();
        HomeTraining homeTraining = homeTrainingRepository.findById(htId).orElse(null);
        if (homeTraining != null) {
            if (homeTrainingSet.contains(homeTraining)) {
                // 이미 책갈피가 있는 경우, 책갈피를 제거합니다.
                homeTrainingSet.remove(homeTraining);
                user.setSaveTraining(homeTrainingSet);
                siteUserRepository.save(user);
                return false; // 책갈피가 제거되었음을 반환합니다.
            } else {
                // 책갈피가 없는 경우, 책갈피를 추가합니다.
                homeTrainingSet.add(homeTraining);
                user.setSaveTraining(homeTrainingSet);
                siteUserRepository.save(user);
                return true; // 책갈피가 추가되었음을 반환합니다.
            }
        }
        return false; // 책갈피를 찾을 수 없는 경우, 실패를 반환합니다.
    }
    public boolean getBookmarkStatus(Long htId, SiteUser user) {
        Set<HomeTraining> homeTrainingSet = user.getSaveTraining();
        HomeTraining homeTraining = homeTrainingRepository.findById(htId).get();
        if(homeTrainingSet.contains(homeTraining)){
            return true;
        }
        return false;
    }
}
