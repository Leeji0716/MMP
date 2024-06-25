package com.example.MMP.ptGroup;

import com.example.MMP.siteuser.SiteUser;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PtGroupRepository extends JpaRepository<PtGroup, Long>,PtGroupCustom {
    PtGroup findByTrainer(SiteUser siteUser);
}
