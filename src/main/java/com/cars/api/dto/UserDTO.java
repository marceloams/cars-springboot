package com.cars.api.dto;

import com.cars.api.model.Role;
import com.cars.api.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private long id;
    private String login;
    private String name;
    private String email;
    private List<String> roles;

    // token jwt
    private String token;

    public static UserDTO create(User user){
        ModelMapper modelMapper = new ModelMapper();
        UserDTO dto = modelMapper.map(user, UserDTO.class);
        dto.roles = (user.getRoles() != null) ?
                user.getRoles().stream()
                    .map(Role::getName)
                    .toList()
                : new ArrayList<>();
        return dto;
    }

    public static UserDTO create(User user, String token) {
        ModelMapper modelMapper = new ModelMapper();
        UserDTO dto = modelMapper.map(user, UserDTO.class);
        dto.token = token;
        return dto;
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper m = new ObjectMapper();
        return m.writeValueAsString(this);
    }
}
