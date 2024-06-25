package com.example.MMP.security;

import com.example.MMP.siteuser.SiteUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

public class UserDetail extends User {
    private final SiteUser siteUser;

    public UserDetail(SiteUser siteUser, Collection<? extends GrantedAuthority> authorities) {
        super(siteUser.getUserId(), siteUser.getPassword(), authorities);
        this.siteUser = siteUser;
    }

    @Override
    public String getPassword() {
        return siteUser.getPassword();
    }

    @Override
    public String getUsername() {
        return siteUser.getUserId();
    }

    public int getPoints() {
        return siteUser.getPoint().getPoints();
    }

    public String getNumber() {
        return siteUser.getNumber();

    }

    public Long getId() {
        return siteUser.getId();
    }

}
