package com.example.MMP.trainer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Trainer> filterTrainers(String gender, String classType, String specialization) {
        if (gender == null && classType == null && specialization == null) {
            return trainerRepository.findAll();
        } else {
            return trainerRepository.findByGenderAndClassTypeAndSpecialization(gender, classType, specialization);
        }
    }
}