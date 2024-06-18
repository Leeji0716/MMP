package com.example.MMP.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;

    public void save(Alarm alarm){
        alarmRepository.save(alarm);
    }

    public void delete(Alarm alarm) {
        alarmRepository.delete(alarm);
    }
}
