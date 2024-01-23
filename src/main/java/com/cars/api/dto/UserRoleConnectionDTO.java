package com.cars.api.dto;

import com.cars.api.model.UserRoleConnection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class UserRoleConnectionDTO {

    @JsonIgnore
    private long id;
    private long userId;
    private long roleId;

    public static UserRoleConnectionDTO create(UserRoleConnection userRoleConnection){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(userRoleConnection, UserRoleConnectionDTO.class);
    }

}
