package com.example.MMP.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig{
        @Bean
        SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                            .requestMatchers("/**").permitAll().anyRequest().authenticated())
                    .formLogin((formLogin) -> formLogin
                            .loginPage("/user/login")
                            .defaultSuccessUrl("/"))
                    .logout((logout) -> logout
                            .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                            .logoutSuccessUrl("/")
                            .invalidateHttpSession(true))
            ;

            return http.build();
        }
}
