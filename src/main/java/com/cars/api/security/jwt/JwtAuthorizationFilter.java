package com.cars.api.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.util.List;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private static Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    private final UserDetailsService userDetailsService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        String token = request.getHeader("Authorization");

        if (StringUtils.isEmpty(token) || !token.startsWith("Bearer ")) {
            //Authorization not granted
            filterChain.doFilter(request, response);
            return;
        }

        try {

            if(! JwtUtil.isTokenValid(token)) {
                throw new AccessDeniedException("Acesso negado.");
            }

            String login = JwtUtil.getLogin(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(login);

            List<GrantedAuthority> authorities = JwtUtil.getRoles(token);

            //var authorities = ((UserDetails) userDetails).getAuthorities();

            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

            // Save Authentication in the Spring context
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);

        } catch (RuntimeException ex) {
            logger.error("Authentication error: " + ex.getMessage(),ex);
            throw ex;
        }
    }
}
