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
        dayPass.setDayPassName(dayPassname);
        dayPass.setDayPassTitle(dayPassTitle);
        dayPass.setDayPassPrice(dayPassPrice);
        dayPass.setDayPassDays(dayPassDays);
        dayPassRepository.save(dayPass);
    }

    public List<DayPass> findAll(){
        return dayPassRepository.findAll();
    }
}
