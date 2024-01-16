package com.cars.api.dto;


import com.cars.api.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    private Long id;
    private String name;
    private List<String> userNames;

    public static RoleDTO create(Role role){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(role, RoleDTO.class);
    }

    public Role toRoleObject(){
        return new Role(this.id, this.name);
    }
}
