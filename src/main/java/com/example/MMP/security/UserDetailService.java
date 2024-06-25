package com.example.MMP.security;

import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final SiteUserRepository siteUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SiteUser> _siteUser = siteUserRepository.findByUserId(username);
        if(_siteUser.isPresent()){
            SiteUser siteUser = _siteUser.get();
            List<GrantedAuthority> authorities = new ArrayList<>();
            if(siteUser.getUserRole().equals("admin")){
                authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
            }else if(siteUser.getUserRole().equals("trainer")){
                authorities.add(new SimpleGrantedAuthority(UserRole.TRAINER.getValue()));
            }else{
                authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
            }
            return new UserDetail(siteUser,authorities);
//            return new User(siteUser.getUserId(),siteUser.getPassword(),authorities);
        }
        return null;
    }
}

