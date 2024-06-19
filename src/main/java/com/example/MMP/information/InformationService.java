package com.example.MMP.information;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InformationService {
    private final InformationRepository informationRepository;

    public void update(String imagePath, String healthName, String companyNumber, String address, String callNumber, String email, String text) {
        Information information = getInformation();
        if (information == null) {
            information = new Information();
        }

        information.setImagePath(imagePath);
        information.setHealthName(healthName);
        information.setCompanyNumber(companyNumber);
        information.setAddress(address);
        information.setCallNumber(callNumber);
        information.setEmail(email);
        information.setText(text);

        informationRepository.save(information);
    }

    public Information getInformation() {
        List<Information> informationList = getInformationList();

        if (informationList.isEmpty()) {
            Information information = new Information();
            informationRepository.save(information);
            informationList = getInformationList();
        }
        Information information = informationList.get(0);
        return information;
    }

    public List<Information> getInformationList() {
        List<Information> informationList = informationRepository.findAll();
        return informationList;
    }

}
