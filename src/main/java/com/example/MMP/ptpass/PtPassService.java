package com.example.MMP.ptpass;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PtPassService {
    public final PtPassRepository ptPassRepository;

    public void createPt(String passName,String passTitle,int passCount,int passPrice,int passDays){
        PtPass ptPass = new PtPass();
        ptPass.setPassName(passName);
        ptPass.setPassTitle(passTitle);
        ptPass.setPassCount(passCount);
        ptPass.setPassPrice(passPrice);
        ptPass.setPassDays(passDays);
        ptPassRepository.save(ptPass);
    }

    public List<PtPass> findAll() {
        return ptPassRepository.findAll();
    }

    public PtPass findById(Long id){
        return ptPassRepository.findById(id).orElseThrow();
    }

    public PtPass findByName(String name){

        Optional<PtPass> ptPass = ptPassRepository.findByPassName(name);
        if(ptPass.isEmpty()){
            return null;
        }else{
            return ptPass.get();
        }
    }

    public void save(PtPass ptPass) {
        ptPassRepository.save(ptPass);
    }

    public void delete(PtPass ptPass) {
        ptPassRepository.delete(ptPass);
    }
}
