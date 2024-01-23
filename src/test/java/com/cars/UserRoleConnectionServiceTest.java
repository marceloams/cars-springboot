package com.cars;

import com.cars.api.dto.UserRoleConnectionDTO;
import com.cars.api.model.UserRoleConnection;
import com.cars.api.service.UserRoleConnectionService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserRoleConnectionServiceTest {

    @Autowired
    UserRoleConnectionService userRoleConnectionService;

    @Test
    public void getAll(){
        List<UserRoleConnectionDTO> connections = userRoleConnectionService.getAll();
        Assertions.assertEquals(3, connections.size());
    }

    @Test
    public void getByUserIdAndRoleId(){
        UserRoleConnectionDTO connectionDTO = userRoleConnectionService.getByUserIdAndRoleId(1L, 1L);
        Assertions.assertEquals(connectionDTO.getUserId(), 1L);
        Assertions.assertEquals(connectionDTO.getRoleId(), 1L);
    }

    private List<Long> verifyClassAttributes(UserRoleConnectionDTO connectionDTO, UserRoleConnection connection){
        //get id
        Long userId = connectionDTO.getUserId();
        Long roleId = connectionDTO.getRoleId();
        Assertions.assertNotNull(userId);
        Assertions.assertNotNull(roleId);

        //get object
        connectionDTO = userRoleConnectionService.getByUserIdAndRoleId(userId, roleId);
        Assertions.assertNotNull(connectionDTO);

        //verify
        Assertions.assertEquals(connection.getUserId(), connectionDTO.getUserId());
        Assertions.assertEquals(connection.getRoleId(), connectionDTO.getRoleId());

        return List.of(userId, roleId);
    }

    @Test
    public void insertAndDelete(){
        UserRoleConnection connection = new UserRoleConnection();
        connection.setUserId(1L);
        connection.setRoleId(2L);

        //add
        UserRoleConnectionDTO connectionDTO = userRoleConnectionService.insert(connection);
        Assertions.assertNotNull(connectionDTO);

        List<Long> ids = verifyClassAttributes(connectionDTO, connection);

        //delete object
        userRoleConnectionService.deleteConnectionByUserIdAndRoleId(ids.get(0), ids.get(1));
        Assertions.assertNull(userRoleConnectionService.getByUserIdAndRoleId(ids.get(0), ids.get(1)));
    }

    @Test
    @Transactional
    public void deleteAllUserConnectionsById(){

        //get before delete
        List<UserRoleConnectionDTO> connections = userRoleConnectionService.getAll().stream().filter(connectionDTO -> connectionDTO.getUserId() == 2L).toList();
        Assertions.assertEquals(2, connections.size());

        //delete
        userRoleConnectionService.deleteAllUserConnectionsById(2L);

        //verify
        connections = userRoleConnectionService.getAll().stream().filter(connectionDTO -> connectionDTO.getUserId() == 2L).toList();
        Assertions.assertEquals(0, connections.size());
    }

    @Test
    @Transactional
    public void deleteAllRoleConnectionsById(){

        //get before delete
        List<UserRoleConnectionDTO> connections = userRoleConnectionService.getAll().stream().filter(connectionDTO -> connectionDTO.getRoleId() == 1L).toList();
        Assertions.assertEquals(2, connections.size());

        //delete
        userRoleConnectionService.deleteAllRoleConnectionsById(1L);

        //verify
        connections = userRoleConnectionService.getAll().stream().filter(connectionDTO -> connectionDTO.getRoleId() == 1L).toList();
        Assertions.assertEquals(0, connections.size());
    }
}
