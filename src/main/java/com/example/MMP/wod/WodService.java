package com.example.MMP.wod;

import com.example.MMP.Comment.Comment;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WodService {
    private final WodRepository wodRepository;
    private final SiteUserRepository siteUserRepository;

    public void create(String imagePath, String content, SiteUser writer) {
        Wod wod = new Wod();
        wod.setImagePath(imagePath);
        wod.setContent(content);
        wod.setCreateDate(LocalDateTime.now());
        wod.setWriter(writer);

        this.wodRepository.save(wod);
    }

    public List<Wod> getList() {
        List<Wod> wodList = wodRepository.findAll();
        return wodList;
    }

    public Wod getWod(Long id) {
        Wod wod = wodRepository.findById(id).get();
        return wod;
    }

    public void delete(Long id) {
        Wod wod = wodRepository.findById(id).get();
        wodRepository.delete(wod);
    }

    public void update(Long id, String content) {
        Wod wod = wodRepository.findById(id).get();
        wod.setContent(content);
        wodRepository.save(wod);
    }

    public Long addLike(Long wodId, String username) {
        Wod wod = wodRepository.findById(wodId).orElseThrow(() -> new RuntimeException("Wod not found"));
        SiteUser user = siteUserRepository.findByUserId(username).get();
        wod.getLikeList().add(user);
        Long likeCount = Long.valueOf(wod.getLikeList().size());
        wod.setLikeCount(likeCount);
        wodRepository.save(wod);
        return wod.getLikeCount();
    }

    public Long removeLike(Long wodId, String username) {
        Wod wod = wodRepository.findById(wodId).orElseThrow(() -> new RuntimeException("Wod not found"));
        SiteUser user = siteUserRepository.findByUserId(username).get();
        wod.getLikeList().remove(user);
        Long likeCount = Long.valueOf(wod.getLikeList().size());
        wod.setLikeCount(likeCount);
        wodRepository.save(wod);
        return wod.getLikeCount();
    }
    public boolean isLikedByUser(Long wodId, String username) {
        Wod wod = wodRepository.findById(wodId).orElseThrow(() -> new IllegalArgumentException("Invalid Wod ID"));
        return wod.getLikeList().stream().anyMatch(user -> user.getUserId().equals(username));
    }

    public int getLikeCount(Long wodId) {
        Wod wod = wodRepository.findById(wodId).orElseThrow(() -> new IllegalArgumentException("Invalid Wod ID"));
        return wod.getLikeList().size();
    }

    public List<Wod> getWodListByCreateDateDesc(SiteUser user) {
        return wodRepository.findByWriterOrderByCreateDateDesc(user);
    }

    public List<Wod> getTop10Wods(List<Wod> wodListAll) {
        return wodListAll.stream()
                .limit(10)
                .collect(Collectors.toList());
    }
    public List<Wod> getAllWodsByCreateDateDesc() {
        return wodRepository.findAllByOrderByCreateDateDesc();
    }
}
