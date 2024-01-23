package com.cars.api.service;

import com.cars.api.dto.UserRoleConnectionDTO;
import com.cars.api.model.UserRoleConnection;
import com.cars.api.repository.UserRoleConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class UserRoleConnectionService {

    @Autowired
    private UserRoleConnectionRepository userRoleConnectionRepository;

    public List<UserRoleConnectionDTO> getAll(){
        return userRoleConnectionRepository.findAll().stream().map(UserRoleConnectionDTO::create).toList();
    }

    public UserRoleConnectionDTO getByUserIdAndRoleId(Long userId, Long roleId){
        UserRoleConnection connection = userRoleConnectionRepository.findByUserIdAndRoleId(userId, roleId);
        return (connection != null) ? UserRoleConnectionDTO.create(connection) : null;
    }

    public UserRoleConnectionDTO insert(UserRoleConnection connection) {
        Assert.isNull(connection.getId(), "Insert was not done!");
        Assert.isNull(getByUserIdAndRoleId(connection.getUserId(), connection.getRoleId()), "Insert was not done! Connection already exists!");

        return UserRoleConnectionDTO.create(userRoleConnectionRepository.save(connection));
    }

    public boolean deleteConnectionByUserIdAndRoleId(Long userId, Long roleId) {
        UserRoleConnectionDTO connectionDTO = getByUserIdAndRoleId(userId, roleId);
        if(connectionDTO != null) {
            userRoleConnectionRepository.deleteById(connectionDTO.getId());
            return true;
        }
        return false;
    }

    public boolean deleteAllUserConnectionsById(Long id) {
        if(userRoleConnectionRepository.countByUserId(id) > 0) {
            userRoleConnectionRepository.deleteByUserId(id);
            return true;
        }
        return false;
    }

    public boolean deleteAllRoleConnectionsById(Long id) {
        if(userRoleConnectionRepository.countByRoleId(id) > 0) {
            userRoleConnectionRepository.deleteByRoleId(id);
            return true;
        }
        return false;
    }

}
