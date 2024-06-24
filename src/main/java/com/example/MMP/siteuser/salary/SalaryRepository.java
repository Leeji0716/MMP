package com.example.MMP.siteuser.salary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SalaryRepository extends JpaRepository<Salary,Long> {

    @Query("SELECT s FROM Salary s ORDER BY s.id DESC")
    Optional<Salary> findTopByOrderByIdDesc();

}
