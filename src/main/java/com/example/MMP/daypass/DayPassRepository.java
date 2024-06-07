package com.example.MMP.daypass;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DayPassRepository extends JpaRepository<DayPass,Long> {

    DayPass findByPassName(String name);
}
