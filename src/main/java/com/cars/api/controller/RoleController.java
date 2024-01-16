package com.cars.api.controller;

import com.cars.api.dto.RoleDTO;
import com.cars.api.model.Role;
import com.cars.api.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAll(){
        List<RoleDTO> roles = roleService.getAll();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getById(@PathVariable Long id){
        RoleDTO roleDTO = roleService.getById(id);
        return new ResponseEntity<>(roleDTO, HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<RoleDTO> getByName(@PathVariable String name){
        RoleDTO roleDTO = roleService.getByName(name);
        return (roleDTO != null)? ResponseEntity.ok(roleDTO) : ResponseEntity.notFound().build();
    }

    private URI getURI(String name) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/name/{name}")
                .buildAndExpand(name).toUri();
    }

    @PostMapping(value = "/add")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<RoleDTO> post(@RequestBody Role role){
        RoleDTO roleDTO = roleService.insert(role);

        return ResponseEntity.created(getURI(roleDTO.getName())).build();
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<RoleDTO> updateById(@PathVariable Long id, @RequestBody Role role){
        role.setId(id);
        RoleDTO roleDTO = roleService.updateById(id, role);
        return (roleDTO != null)? ResponseEntity.ok(roleDTO) : ResponseEntity.notFound().build();
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(value = "/deleteByName/{name}")
    public ResponseEntity deleteByName(@PathVariable String name){
        return switch (roleService.deleteByName(name)){
            case OK -> ResponseEntity.ok().build();
            case ASSIGNED -> ResponseEntity.unprocessableEntity().body("This role is assigned to a user!");
            default -> ResponseEntity.notFound().build();
        };
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity deleteById(@PathVariable Long id){
        return switch (roleService.deleteById(id)){
            case OK -> ResponseEntity.ok().build();
            case ASSIGNED -> ResponseEntity.unprocessableEntity().body("This role is assigned to a user!");
            default -> ResponseEntity.notFound().build();
        };
    }

}
