package com.example.MMP.transPass;

import com.example.MMP.siteuser.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransPassService {
    private final TransPassRepository transPassRepository;
    public void save(TransPass transPass){
     transPassRepository.save(transPass);
    }

    public List<TransPass> MySendPass(SiteUser siteUser){
        return transPassRepository.MySendPass(siteUser);
    }
    public List<TransPass> MyAcceptPass(SiteUser siteUser){
        return transPassRepository.MyAcceptPass(siteUser);
    }
}
