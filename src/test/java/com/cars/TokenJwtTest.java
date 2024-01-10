package com.cars;

import com.cars.api.security.jwt.JwtUtil;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarsApplication.class)
public class TokenJwtTest {

    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;

    @Test
    public void testToken() {

        // get user
        UserDetails user = userDetailsService.loadUserByUsername("admin");
        Assertions.assertNotNull(user);

        // token generation
        String jwtToken = JwtUtil.createToken(user);
        System.out.println(jwtToken);
        Assertions.assertNotNull(jwtToken);

        // token validation
        boolean ok = JwtUtil.isTokenValid(jwtToken);
        Assertions.assertTrue(ok);

        // login validation
        String login = JwtUtil.getLogin(jwtToken);
        Assertions.assertEquals("admin",login);

        // roles validation
        List<GrantedAuthority> roles = JwtUtil.getRoles(jwtToken);
        Assertions.assertNotNull(roles);
        System.out.println(roles);
        String role = roles.get(0).getAuthority();
        Assertions.assertEquals(role,"ROLE_ADMIN");
    }

}
