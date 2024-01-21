package com.cars.api.service;

import com.cars.api.dto.UserDTO;
import com.cars.api.exception.ObjectNotFoundException;
import com.cars.api.model.Role;
import com.cars.api.model.User;
import com.cars.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> getAll(){
        return userRepository.findAll().stream().map(UserDTO::create).toList();
    }

    public List<UserDTO> getUsersByRole(String role) {
        return userRepository.findAll().stream().map(UserDTO::create).filter(userDto -> userDto.getRoles().contains(role)).collect(Collectors.toList());
    }

    public UserDTO getById(Long id){
        return userRepository.findById(id).map(UserDTO::create).orElseThrow(() -> new ObjectNotFoundException("User not found!"));
    }

    public UserDTO getByLogin(String login){
        User user = userRepository.findByLogin(login);
        if(user == null)
            throw new UsernameNotFoundException("User not found!");
        return UserDTO.create(user);
    }

    private String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return "{bcrypt}" + encoder.encode(password);
    }

    public UserDTO insert(User user) {
        Assert.isNull(user.getId(), "Insert was not done!");
        Assert.notNull(user.getPassword(), "Insert was not done!");

        user.setPassword(encryptPassword(user.getPassword()));

        if(user.getRoles() == null){
            Role roleUser = new Role(1L, "ROLE_USER");
            user.setRoles(Collections.singletonList(roleUser));
        } else if (user.getRoles().stream().noneMatch(role -> Objects.equals(role.getId(), 1L))) {
            Role roleUser = new Role(1L, "ROLE_USER");
            user.getRoles().add(roleUser);
        }

        return UserDTO.create(userRepository.save(user));
    }

    public UserDTO updateById(Long id, User user) {
        Assert.notNull(id, "Not possible to update!");

        Optional<User> optional = userRepository.findById(id);

        if(optional.isPresent()){
            User db = optional.get();

            db.setLogin(user.getLogin());
            db.setName(user.getName());
            db.setEmail(user.getEmail());

            userRepository.save(db);

            return UserDTO.create(db);
        } else {
            return null;
        }
    }


    public boolean deleteById(Long id) {
        if(getById(id) != null) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
