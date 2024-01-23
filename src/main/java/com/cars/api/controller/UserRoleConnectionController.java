package com.cars.api.controller;

import com.cars.api.dto.UserRoleConnectionDTO;
import com.cars.api.model.UserRoleConnection;
import com.cars.api.service.UserRoleConnectionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user-roles-connection")
public class UserRoleConnectionController {

    @Autowired
    private UserRoleConnectionService userRoleConnectionService;

    @GetMapping
    public ResponseEntity<List<UserRoleConnectionDTO>> getAll(){
        List<UserRoleConnectionDTO> connections = userRoleConnectionService.getAll();
        return ResponseEntity.ok(connections);
    }

    @GetMapping("/connection")
    public ResponseEntity<UserRoleConnectionDTO> getByUserIdAndRoleId(@RequestParam Long userId, @RequestParam Long roleId){
        UserRoleConnectionDTO connection = userRoleConnectionService.getByUserIdAndRoleId(userId, roleId);
        return (connection != null)? ResponseEntity.ok(connection) : ResponseEntity.notFound().build();
    }

    private URI getURI(Long userId, Long roleId) {
        return ServletUriComponentsBuilder.fromCurrentRequest().replacePath("/api/v1/user-roles-connection/connection")
                .queryParam("userId", userId)
                .queryParam("roleId", roleId)
                .build().toUri();
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping(value = "/add")
    public ResponseEntity<UserRoleConnectionDTO> post(@RequestBody UserRoleConnection connection){
        UserRoleConnectionDTO connectionDTO = userRoleConnectionService.insert(connection);

        return ResponseEntity.created(getURI(connectionDTO.getUserId(), connectionDTO.getRoleId())).build();
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(value = "/delete")
    public ResponseEntity deleteConnectionByUserIdAndRoleId(@RequestParam Long userId, @RequestParam Long roleId){
        return (userRoleConnectionService.deleteConnectionByUserIdAndRoleId(userId, roleId))? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @Transactional
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(value = "/delete/all/user/{id}")
    public ResponseEntity deleteAllUserConnectionsById(@PathVariable Long id){
        return (userRoleConnectionService.deleteAllUserConnectionsById(id))? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @Transactional
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(value = "/delete/all/role/{id}")
    public ResponseEntity deleteAllRoleConnectionsById(@PathVariable Long id){
        return (userRoleConnectionService.deleteAllRoleConnectionsById(id))? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}

