package com.example.MMP.trainer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerService {

    private final  TrainerRepository trainerRepository;

    public void create(String imagePath,
                       String name,
                       String introduce) {

        Trainer trainer = new Trainer();
        trainer.setImagePath(imagePath);
        trainer.setTrainerName(name);
        trainer.setIntroduce(introduce);

        this.trainerRepository.save(trainer);
    }

    public List<Trainer> getList() {
        return trainerRepository.findAll();
    }

    public Trainer getTrainer(Long id) {
        Trainer trainer = trainerRepository.findById(id).get();
        return trainer;
    }

    public void delete(Long id) {
        Trainer trainer = trainerRepository.findById(id).get();
        trainerRepository.delete(trainer);
    }

    public void update(Long id,
                       String introduce) {

        Trainer trainer = trainerRepository.findById(id).get();
        trainer.setIntroduce(introduce);
        trainerRepository.save(trainer);
    }

    public List<Trainer> findAll() {
        return this.trainerRepository.findAll();
    }
}