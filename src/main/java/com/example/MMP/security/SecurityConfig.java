package com.example.MMP.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig{

    private final UserDetailService userDetailService;
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
                    .rememberMe((rememberMe) -> rememberMe
                            .key("uniqueAndSecret")
                            .tokenValiditySeconds(30 * 24 * 60 * 60) // 30 days
                            .rememberMeParameter("remember-me")
                            .userDetailsService(userDetailService))
                    .csrf(c -> c.ignoringRequestMatchers(

                            new AntPathRequestMatcher("/pt/**"),
                            new AntPathRequestMatcher("/totalPass/**"),
                            new AntPathRequestMatcher("/day/**"),
                            new AntPathRequestMatcher("/pt/**"),
                            new AntPathRequestMatcher("/notice/**"), // CSRF 보호에서 제외
                            new AntPathRequestMatcher("/user/**"),
                            new AntPathRequestMatcher("/challenge/**"),
                            new AntPathRequestMatcher("/success"),
                            new AntPathRequestMatcher("/**")
                    ))
            ;

            return http.build();
        }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
