package com.example.MMP.ptGroup;

import com.example.MMP.siteuser.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PtGroupService {
    private final PtGroupRepository ptGroupRepository;

    public PtGroup findByTrainer(SiteUser siteUser) {
        return ptGroupRepository.findByTrainer(siteUser);
    }

    public void save(PtGroup ptGroup) {
        ptGroupRepository.save(ptGroup);
    }
}
