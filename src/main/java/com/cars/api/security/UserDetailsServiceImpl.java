package com.cars.api.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(value = "userDetailsService")
public class UserDetailsServiceImpl  implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return switch (username) {
            case "user" -> User.builder()
                    .username("user")
                    .password("{bcrypt}$2a$10$tLk/NrhSisnvCRumlGCpPuqALl4FEKyG/KI0Bc1c63YLzCjlIx1cG")
                    .roles("USER")
                    .build();
            case "admin" -> User.builder()
                    .username("admin")
                    .password("{bcrypt}$2a$10$O6gLPPnqMH3shxsdRNAOpu5hyR4nnNusIXHCKjg.y3jfKKZGrllBu")
                    .roles("USER", "ADMIN")
                    .build();
            default -> throw new UsernameNotFoundException("User not found!");
        };
    }
}
