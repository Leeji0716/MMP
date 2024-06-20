package com.example.MMP.trainer;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FilterRequest {
    private List<String> gender;
    private List<String> classType;
    private List<String> specialization;
}
