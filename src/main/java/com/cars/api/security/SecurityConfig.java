package com.cars.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests((auth) -> auth
                .anyRequest().authenticated()
            )
            .csrf(AbstractHttpConfigurer::disable
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            )
            .httpBasic(withDefaults());

        return http.build();
    }

    //Way of doing it without implementing UserDetailsService
//    @Bean
//    public UserDetailsService users() {
//
//        UserDetails user = User.builder()
//                .username("user")
//                .password("{bcrypt}$2a$10$tLk/NrhSisnvCRumlGCpPuqALl4FEKyG/KI0Bc1c63YLzCjlIx1cG")
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("{bcrypt}$2a$10$O6gLPPnqMH3shxsdRNAOpu5hyR4nnNusIXHCKjg.y3jfKKZGrllBu")
//                .roles("USER", "ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
}
