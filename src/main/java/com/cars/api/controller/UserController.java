package com.cars.api.controller;

import com.cars.api.dto.UserDTO;
import com.cars.api.model.User;
import com.cars.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll(){
        List<UserDTO> registeredUsers = userService.getAll();
        return ResponseEntity.ok(registeredUsers);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable String role){
        List<UserDTO> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping("/login/{login}")
    public ResponseEntity<UserDTO> getByLogin(@PathVariable String login){
        return ResponseEntity.ok(userService.getByLogin(login));
    }

    @GetMapping("/logged-user")
    public UserDTO getLoggedUserInfo(@AuthenticationPrincipal User user){
        return UserDTO.create(user);
    }

    private URI getURI(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().replacePath("/api/v1/users/{id}")
                .buildAndExpand(id).toUri();
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping(value = "/add")
    public ResponseEntity<UserDTO> post(@RequestBody User user){
        UserDTO userDTO = userService.insert(user);
        return ResponseEntity.created(getURI(userDTO.getId())).build();
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<UserDTO> updateById(@PathVariable Long id, @RequestBody User user){
        user.setId(id);
        UserDTO userDTO = userService.updateById(id, user);
        return (userDTO != null)? ResponseEntity.ok(userDTO) : ResponseEntity.notFound().build();
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity deleteById(@PathVariable Long id){
        return (userService.deleteById(id))? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

}
