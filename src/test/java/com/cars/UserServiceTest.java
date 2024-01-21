package com.cars;

import com.cars.api.dto.UserDTO;
import com.cars.api.exception.ObjectNotFoundException;
import com.cars.api.model.User;
import com.cars.api.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    public void getAll(){
        List<UserDTO> users = userService.getAll();
        Assertions.assertEquals(2, users.size());
    }

    @Test
    public void getUsersByRole(){
        List<UserDTO> users = userService.getUsersByRole("ROLE_USER");
        Assertions.assertEquals(2, users.size());

        users = userService.getUsersByRole("ROLE_ADMIN");
        Assertions.assertEquals(1, users.size());

        users = userService.getUsersByRole("ROLE_NON_EXISTENT");
        Assertions.assertEquals(0, users.size());
    }

    @Test
    public void getById(){
        UserDTO userDTO = userService.getById(1L);
        Assertions.assertEquals(1L, userDTO.getId());

        Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.getById(100L));
    }

    @Test
    public void getByLogin(){
        UserDTO userDTO = userService.getByLogin("admin");
        Assertions.assertEquals("admin", userDTO.getLogin());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.getByLogin("ROLE_NON_EXISTENT"));
    }

    private Long verifyClassAttributes(UserDTO userDTO, User user){
        //get id
        Long id = userDTO.getId();
        Assertions.assertNotNull(id);

        //get object
        userDTO = userService.getById(id);
        Assertions.assertNotNull(userDTO);

        //verify
        Assertions.assertEquals(user.getName(), userDTO.getName());
        Assertions.assertEquals(user.getEmail(), userDTO.getEmail());
        Assertions.assertEquals(user.getLogin(), userDTO.getLogin());

        return id;
    }

    @Test
    public void insertUpdateAndDelete(){
        User user = new User();
        user.setName("New User");
        user.setEmail("new_user@email.com");
        user.setLogin("newUser");
        user.setPassword("new-user-password");

        //add
        UserDTO userDTO = userService.insert(user);
        Assertions.assertNotNull(userDTO);

        Long id = verifyClassAttributes(userDTO, user);

        //update
        user.setName("New User Update");
        user.setEmail("new_user_update@email.com");
        user.setLogin("newUserUpdate");
        userDTO = userService.updateById(id, user);

        verifyClassAttributes(userDTO, user);

        //delete object
        userService.deleteById(id);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.getById(id));
    }
}
