package com.cars;

import com.cars.api.dto.UserDTO;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserAPITest extends BaseAPITest {

    private ResponseEntity<List<UserDTO>> getUsers(String url){
        HttpHeaders headers = getHeaders();

        return rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                });
    }

    @Test
    public void listUsers() {
        List<UserDTO> usersDto = getUsers("/api/v1/users").getBody();
        Assertions.assertNotNull(usersDto);
        Assertions.assertEquals(2, usersDto.size());
    }

    @Test
    public void getLoggedUser() {
        ResponseEntity<UserDTO> response = get("/api/v1/users/logged-user", UserDTO.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        UserDTO userDTO = response.getBody();
        assert userDTO != null;
        Assertions.assertEquals("Admin", userDTO.getName());
    }

}
