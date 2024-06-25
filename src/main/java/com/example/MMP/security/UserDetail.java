package com.example.MMP.security;

import com.example.MMP.point.Point;
import com.example.MMP.siteuser.SiteUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetail implements UserDetails {
    private final SiteUser siteUser;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;

    private final boolean credentialsNonExpired;

    private final boolean enabled;

    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetail(SiteUser siteUser, Collection<? extends GrantedAuthority> authorities) {
        this.siteUser = siteUser;
        this.authorities = authorities;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
