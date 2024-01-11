package com.cars.api.controller;

import com.cars.api.dto.UserDTO;
import com.cars.api.model.User;
import com.cars.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers(){
        List<UserDTO> registeredUsers = userService.getUsers();
        return ResponseEntity.ok(registeredUsers);
    }

    @GetMapping("/loggedUserInfo")
    public UserDTO getLoggedUserInfo(@AuthenticationPrincipal User user){
        return UserDTO.create(user);
    }

}
