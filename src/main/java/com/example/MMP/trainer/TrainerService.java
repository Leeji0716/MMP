package com.example.MMP.trainer;

import com.example.MMP.siteuser.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepository trainerRepository;

    public void create(String imagePath,
                       SiteUser userTrainer,
                       String introduce,
                       String gender,
                       String classType,
                       String specialization) {

        Trainer trainer = new Trainer();
        trainer.setImagePath(imagePath);
        trainer.setUserTrainer(userTrainer);
        trainer.setIntroduce(introduce);
        trainer.setClassType(classType);
        trainer.setSpecialization(specialization);

        // keyword 생성 및 설정
        String keyword = generateKeyword(gender, classType, specialization);
        trainer.setKeyword(keyword);

        // Trainer 객체 저장
        this.trainerRepository.save(trainer);
    }

    // keyword 생성 메서드
    private String generateKeyword(String gender, String classType, String specialization) {
        return gender + "," + classType + "," + specialization;
    }

    public List<Trainer> getList() {
        return trainerRepository.findAll();
    }

    public Trainer getTrainer(Long id) {
        return trainerRepository.findById(id).orElse(null);
    }

    public void delete(Trainer trainer) {
        trainerRepository.delete(trainer);
    }

    public List<Trainer> findAll() {
        return this.trainerRepository.findAll();
    }

    public List<Trainer> filterTrainers(List<Trainer> trainers, FilterRequest filterRequest) {
        return trainers.stream()
                .filter(trainer -> filterRequest.getKeyword() == null
                        || filterRequest.getKeyword().isEmpty()
                        || Arrays.stream(trainer.getKeyword().split(",\\s*"))
                        .anyMatch(keyword -> filterRequest.getKeyword().contains(keyword)))

                // 필터링된 트레이너들을 리스트로 수집합니다.
                .collect(Collectors.toList());
    }


    public Trainer findById(Long id) {
        Trainer trainer = trainerRepository.findById(id).orElseThrow();
        return trainer;
    }

    public void update(Trainer trainer, String introduce, String classType, String specialization, String imagePath) {
        trainer.setIntroduce(introduce);
        trainer.setClassType(classType);
        trainer.setSpecialization(specialization);

        // keyword 생성 및 설정
        String keyword = generateKeyword(trainer.getUserTrainer().getGender(), classType, specialization);
        trainer.setKeyword(keyword);
        trainer.setImagePath(imagePath);

        trainerRepository.save(trainer);
    }
}