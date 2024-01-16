package com.cars.api.service;

import com.cars.api.dto.RoleDTO;
import com.cars.api.exception.ObjectNotFoundException;
import com.cars.api.model.Role;
import com.cars.api.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public enum StatusAnswer {
        OK,
        ASSIGNED,
        NOT_FOUND
    }

    private List<String> getUsernames(Role role){
        return roleRepository.findUsernamesByRole(role.getName());
    }

    private String getFormattedName(String name){
        return (name.contains("_")) ?  name.toUpperCase() : "ROLE_" + name.toUpperCase();
    }

    private RoleDTO roleToRoleDTO(Role role){
        RoleDTO roleDTO = RoleDTO.create(role);
        roleDTO.setUserNames(getUsernames(role));
        return roleDTO;
    }

    public List<RoleDTO> getAll(){
        return roleRepository.findAll().stream().map(this::roleToRoleDTO).toList();
    }

    public RoleDTO getByName(String name){
        Role role = roleRepository.findByName(getFormattedName(name));
        return (role != null) ? roleToRoleDTO(role) : null;
    }

    public RoleDTO getById(Long id){
        Optional<Role> optional = roleRepository.findById(id);
        return optional.map(this::roleToRoleDTO).orElseThrow(() -> new ObjectNotFoundException("Role not found!"));
    }

    public RoleDTO insert(Role role){
        Assert.isNull(role.getId(), "Insert was not done!");
        String roleFormattedName = getFormattedName(role.getName());
        role.setName(roleFormattedName);
        return RoleDTO.create(roleRepository.save(role));
    }

    public RoleDTO updateById(Long id, Role role) {
        Assert.notNull(id, "Not possible to update!");

        Optional<Role> optional = roleRepository.findById(id);

        if(optional.isPresent()){
            Role db = optional.get();
            String formattedRoleName = getFormattedName(role.getName());
            db.setName(formattedRoleName);
            roleRepository.save(db);

            return roleToRoleDTO(role);
        } else {
            return null;
        }
    }

    public StatusAnswer deleteByName(String name) {
        RoleDTO roleDTO = getByName(getFormattedName(name));
        if(roleDTO != null) {
            if(roleDTO.getUserNames().isEmpty()){
                roleRepository.delete(roleDTO.toRoleObject());
                return StatusAnswer.OK;
            }
            return StatusAnswer.ASSIGNED;
        }
        return StatusAnswer.NOT_FOUND;
    }

    public StatusAnswer deleteById(Long id) {
        RoleDTO roleDTO = getById(id);
        if(roleDTO != null) {
            if(roleDTO.getUserNames().isEmpty()){
                roleRepository.delete(roleDTO.toRoleObject());
                return StatusAnswer.OK;
            }
            return StatusAnswer.ASSIGNED;
        }
        return StatusAnswer.NOT_FOUND;
    }
}