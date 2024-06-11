package com.example.MMP.homeTraining;

import com.example.MMP.homeTraining.category.Category;
import com.example.MMP.homeTraining.category.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    public void create(String content, String videoUrl, SiteUser writer, Category category) {
        HomeTraining homeTraining = new HomeTraining();
        homeTraining.setContent(content);
        homeTraining.setVideoUrl(videoUrl);
        homeTraining.setCreateDate(LocalDateTime.now());
        homeTraining.setWriter(writer);
        homeTraining.setCategory(category);

        homeTraining.setThumbnailUrl(intro(videoUrl));
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
        List<HomeTraining> homeTrainingSet = user.getSaveTraining();
        HomeTraining homeTraining = homeTrainingRepository.findById(htId).orElse(null);
        if (homeTraining != null) {
            if (homeTrainingSet.contains(homeTraining)) {
                return true; // 책갈피가 제거되었음을 반환합니다.
            } else {
                return false; // 책갈피가 추가되었음을 반환합니다.
            }
        }
        return false; // 책갈피를 찾을 수 없는 경우, 실패를 반환합니다.
    }
    public boolean getBookmarkStatus(Long htId, SiteUser user) {
        List<HomeTraining> homeTrainingSet = user.getSaveTraining();
        HomeTraining homeTraining = homeTrainingRepository.findById(htId).get();
        if(homeTrainingSet.contains(homeTraining)){
            return true;
        }
        return false;
    }

    public List<HomeTraining> getCategoryList(int id){
        Category category = categoryRepository.findById(id);
        List<HomeTraining> homeTrainingList = homeTrainingRepository.findByCategory(category);
        return homeTrainingList;
    }

    public HomeTraining getHomeTraining(Long id) {
        HomeTraining homeTraining = homeTrainingRepository.findById(id).get();
        return homeTraining;
    }

    public void delete(HomeTraining homeTraining) {
        homeTrainingRepository.delete(homeTraining);
    }

    public void update(HomeTraining homeTraining, int categoryID, String content, String videoUrl) {
        Category category = categoryRepository.findById(categoryID);
        homeTraining.setCategory(category);
        homeTraining.setContent(content);
        homeTraining.setVideoUrl(videoUrl);

        homeTraining.setThumbnailUrl(intro(videoUrl));
        homeTrainingRepository.save(homeTraining);
    }

    public String intro(String videoUrl){
        String videoId = videoUrl.split("v=")[1];
        String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/0.jpg";
        return thumbnailUrl;
    }

    public List<HomeTraining> getSaveTraining(SiteUser user) {
        List<HomeTraining> saveTraining = user.getSaveTraining();
        return saveTraining;
    }
}
