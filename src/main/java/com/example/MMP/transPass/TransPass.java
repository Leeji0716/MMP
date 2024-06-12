package com.example.MMP.transPass;

import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.userPass.UserDayPass;
import com.example.MMP.userPass.UserPtPass;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class TransPass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private SiteUser sendUser;

    @ManyToOne
    private SiteUser acceptUser;

    private Boolean consent;

    @OneToOne
    @JoinColumn(name = "userDayPassId")
    private UserDayPass userDayPass;

    @OneToOne
    @JoinColumn(name = "userPtPassId")
    private UserPtPass userPtPass;

    @Builder
    public TransPass(SiteUser sendUser, SiteUser acceptUser) {
        this.sendUser = sendUser;
        this.acceptUser = acceptUser;
        this.consent = false;
    }

}