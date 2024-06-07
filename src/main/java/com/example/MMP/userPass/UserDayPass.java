package com.example.MMP.userPass;

import com.example.MMP.siteuser.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class UserDayPass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String passName;

    private String passTitle;

    private int passPrice;

    private LocalDateTime passStart;
    private LocalDateTime passFinish;

    @ManyToOne
    private SiteUser siteUser;


}
