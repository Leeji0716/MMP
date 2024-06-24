package com.example.MMP.trainer;

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
                       String name,
                       String introduce,
                       String gender,
                       String classType,
                       String specialization) {

        Trainer trainer = new Trainer();
        trainer.setImagePath(imagePath);
        trainer.setTrainerName(name);
        trainer.setIntroduce(introduce);
        trainer.setGender(gender);
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

    public void delete(Long id) {
        trainerRepository.deleteById(id);
    }

    public void update(Long id, String introduce) {
        Trainer trainer = trainerRepository.findById(id).orElse(null);
        if (trainer != null) {
            trainer.setIntroduce(introduce);
            trainerRepository.save(trainer);
        }
    }

    public List<Trainer> findAll() {
        return this.trainerRepository.findAll();
    }

//    public List<Trainer> filterTrainers(List<Trainer> trainers, FilterRequest filterRequest) {
//        return trainers.stream()
//                .filter(trainer -> filterRequest.getKeyword() == null
//                        || filterRequest.getKeyword().isEmpty()
//                        || Arrays.stream(trainer.getKeyword().split(",\\s*"))
//                        .anyMatch(keyword -> filterRequest.getKeyword().contains(keyword)))
//
//                // 필터링된 트레이너들을 리스트로 수집합니다.
//                .collect(Collectors.toList());
//    }

    public List<Trainer> filterTrainers(List<Trainer> trainers, FilterRequest filterRequest) {
        List<String> filterKeywords = filterRequest.getKeyword();

        if (filterKeywords == null || filterKeywords.isEmpty()) {
            return trainers; // No filter applied
        }

        return trainers.stream()
                .filter(trainer -> {
                    List<String> trainerKeywords = Arrays.asList(trainer.getKeyword().split(",\\s*"));

                    // Exact match for gender
                    boolean exactGenderMatch = filterKeywords.contains(trainer.getGender().toLowerCase());

                    // Other keyword filtering (excluding gender)
                    boolean keywordMatches = filterKeywords.stream()
                            .filter(keyword -> !keyword.equalsIgnoreCase("male") && !keyword.equalsIgnoreCase("female"))
                            .anyMatch(trainerKeywords::contains);

                    return exactGenderMatch || keywordMatches;
                })
                .collect(Collectors.toList());
    }
}