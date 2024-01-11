package com.cars;

import com.cars.api.dto.UserDTO;
import com.cars.api.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    public void listUsers(){
        List<UserDTO> users = userService.getUsers();

        Assertions.assertEquals(2, users.size());
    }
}
