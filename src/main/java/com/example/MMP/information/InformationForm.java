package com.example.MMP.information;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InformationForm {
    @Size(max = 200)
    private String imagePath;

    @Size(max = 20)
    private String healthName;

    @Size(max = 12)
    private String companyNumber;

    @Size(max = 100)
    private String address;

    @Size(max = 15)
    private String callNumber;

    @Size(max = 30)
    private String email;

    @Size(max = 200)
    private String text;
}
