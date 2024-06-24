package com.example.MMP.daypass;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DayPassService {
private final DayPassRepository dayPassRepository;
    public void create(String dayPassname, String dayPassTitle,int dayPassPrice,int dayPassDays){
        DayPass dayPass = new DayPass();
        dayPass.setPassName(dayPassname);
        dayPass.setPassTitle(dayPassTitle);
        dayPass.setPassPrice(dayPassPrice);
        dayPass.setPassDays(dayPassDays);
        dayPassRepository.save(dayPass);
    }

    public List<DayPass> findAll(){
        return dayPassRepository.findAll();
    }

    public DayPass findByName(String name){
        return dayPassRepository.findByPassName(name);
    }

    public DayPass findById(Long id) {
        return dayPassRepository.findById(id).orElseThrow();
    }

    public void save(DayPass dayPass) {
        dayPassRepository.save(dayPass);
    }

    public void delete(DayPass dayPass) {
        dayPassRepository.delete(dayPass);
    }
}
