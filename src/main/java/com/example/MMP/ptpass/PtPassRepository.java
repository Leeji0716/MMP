package com.example.MMP.ptpass;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PtPassRepository extends JpaRepository<PtPass,Long> {
    Optional<PtPass> findByPassName(String name);
}
