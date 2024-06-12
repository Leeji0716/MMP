package com.example.MMP.transPass;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransPassRepository extends JpaRepository<TransPass,Long>,TransPassCustom {
}
