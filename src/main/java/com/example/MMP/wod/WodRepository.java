package com.example.MMP.wod;

import com.example.MMP.siteuser.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WodRepository extends JpaRepository<Wod, Long>,WodCustom {
    List<Wod> findByWriterOrderByCreateDateDesc(SiteUser writer);
    List<Wod> findAllByOrderByCreateDateDesc();
}
