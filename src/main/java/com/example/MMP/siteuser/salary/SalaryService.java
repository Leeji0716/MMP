package com.example.MMP.siteuser.salary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalaryService {
    private final SalaryRepository salaryRepository;

    public Salary create(int bonus, int incentive) {
        Salary salary = new Salary ();
        salary.setBonus (bonus*10000);
        salary.setIncentive (incentive*10000);
     return salaryRepository.save (salary);
    }

    public Salary getLastSalary() {
        Optional<Salary> lastSalary = salaryRepository.findTopByOrderByIdDesc();
        return lastSalary.orElse(null); // Return the last salary or null if not found
    }
}
