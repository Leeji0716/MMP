package com.example.MMP.trainer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        this.trainerRepository.save(trainer);
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
        return this.trainerRepository.findAll();//
    }

    public List<Trainer> filterTrainers(FilterRequest filterRequest) {
        // 모든 트레이너 목록 가져오기
        List<Trainer> allTrainers = trainerRepository.findAll();

        // 필터 조건에 따라 트레이너 목록을 필터링
        return allTrainers.stream()
                .filter(trainer -> filterRequest.getGender().isEmpty() || filterRequest.getGender().contains(trainer.getGender()))
                .filter(trainer -> filterRequest.getClassType().isEmpty() || filterRequest.getClassType().contains(trainer.getClassType()))
                .filter(trainer -> filterRequest.getSpecialization().isEmpty() || filterRequest.getSpecialization().contains(trainer.getSpecialization()))
                .collect(Collectors.toList());
    }
}