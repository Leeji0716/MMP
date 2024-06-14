package com.example.MMP.transPass;

import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.userPass.UserDayPass;
import com.example.MMP.userPass.UserPtPass;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class TransPass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    private SiteUser sendUser;

    @ManyToOne
    @JsonBackReference
    private SiteUser acceptUser;

    private Boolean consent;

    @OneToOne
    @JoinColumn(name = "userDayPassId")
    @JsonManagedReference
    private UserDayPass userDayPass;

    @OneToOne
    @JoinColumn(name = "userPtPassId")
    @JsonManagedReference
    private UserPtPass userPtPass;

    @Builder
    public TransPass(SiteUser sendUser, SiteUser acceptUser) {
        this.sendUser = sendUser;
        this.acceptUser = acceptUser;
        this.consent = false;
    }

    @CreatedDate
    private LocalDate createDate;

}
