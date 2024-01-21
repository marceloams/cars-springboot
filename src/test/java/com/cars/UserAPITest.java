package com.cars;

import com.cars.api.dto.UserDTO;
import com.cars.api.model.User;
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

    private final String basePathUrl = "/api/v1/users";

    private ResponseEntity<UserDTO> getUser(String url){
        return get(url, UserDTO.class);
    }

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
    public void getAll() {
        List<UserDTO> usersDto = getUsers(basePathUrl).getBody();
        Assertions.assertNotNull(usersDto);
        Assertions.assertEquals(2, usersDto.size());
    }

    @Test
    public void getUsersByRole() {
        List<UserDTO> usersDto = getUsers(basePathUrl + "/role/ROLE_USER").getBody();
        Assertions.assertNotNull(usersDto);
        Assertions.assertEquals(2, usersDto.size());
    }

    @Test
    public void getById() {
        UserDTO userDto = getUser(basePathUrl + "/1").getBody();
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(1L, userDto.getId());
    }

    @Test
    public void getByLogin() {
        UserDTO userDto = getUser(basePathUrl + "/login/user").getBody();
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(1L, userDto.getId());
    }

    @Test
    public void getLoggedUser() {
        ResponseEntity<UserDTO> response = get(basePathUrl + "/logged-user", UserDTO.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        UserDTO userDTO = response.getBody();
        assert userDTO != null;
        Assertions.assertEquals("Admin", userDTO.getName());
    }

    private Long verifyClassAttributes(String location, User user) {
        //get object
        UserDTO userDTO = getUser(location).getBody();

        //verify
        Assertions.assertNotNull(userDTO);
        Assertions.assertEquals(user.getName(), userDTO.getName());
        Assertions.assertEquals(user.getEmail(), userDTO.getEmail());
        Assertions.assertEquals(user.getLogin(), userDTO.getLogin());

        return userDTO.getId();
    }

    @Test
    public void addUpdateAndDelete() {
        User user = new User();
        user.setName("New User");
        user.setEmail("new_user@email.com");
        user.setLogin("newUser");
        user.setPassword("new-user-password");

        //add
        ResponseEntity<UserDTO> response = post(basePathUrl + "/add", user, UserDTO.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        //verify addition
        String location = response.getHeaders().getLocation() + "";
        Long id = verifyClassAttributes(location, user);

        //update
        user.setName("New User Update");
        user.setEmail("new_user_update@email.com");
        user.setLogin("newUserUpdate");
        response = put(basePathUrl + "/update/" + id, user, UserDTO.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        //verify update
        verifyClassAttributes(location, user);

        //delete
        delete(basePathUrl + "/delete/" + id, null);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, getUser(location).getStatusCode());
    }
}
