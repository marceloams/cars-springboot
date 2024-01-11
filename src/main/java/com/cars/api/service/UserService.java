package com.cars.api.service;

import com.cars.api.dto.UserDTO;
import com.cars.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> getUsers(){
        return userRepository.findAll().stream().map(UserDTO::create).collect(Collectors.toList());
    }

}
