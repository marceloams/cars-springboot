package com.cars.api.security.jwt.handler;

import com.cars.api.security.jwt.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UnauthorizedHandler implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(UnauthorizedHandler.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        if(response.getStatus()==404) return;

        logger.warn("UnauthorizedHandler, exception: " + authException);

        //called if wrong token or if it is null
        String json = ServletUtil.getJson("error", "Unauthorized!");
        ServletUtil.write(response, HttpStatus.FORBIDDEN, json);
    }
}
