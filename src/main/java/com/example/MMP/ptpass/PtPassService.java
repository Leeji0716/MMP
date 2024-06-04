package com.example.MMP.ptpass;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PtPassService {
    public final PtPassRepository ptPassRepository;

    public void createPt(String passName,String passTitle,int passCount,int passPrice){
        PtPass ptPass = new PtPass();
        ptPass.setPassName(passName);
        ptPass.setPassTitle(passTitle);
        ptPass.setPassCount(passCount);
        ptPass.setPassPrice(passPrice);
        ptPassRepository.save(ptPass);
    }

    public List<PtPass> findAll(){
        return ptPassRepository.findAll();
    }
}
