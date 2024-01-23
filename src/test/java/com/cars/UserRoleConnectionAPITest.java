package com.cars;

import com.cars.api.dto.UserRoleConnectionDTO;
import com.cars.api.model.UserRoleConnection;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRoleConnectionAPITest extends BaseAPITest {

    private final String basePathUrl = "/api/v1/user-roles-connection";

    private ResponseEntity<UserRoleConnectionDTO> getConnection(String url){
        return get(url, UserRoleConnectionDTO.class);
    }

    private ResponseEntity<List<UserRoleConnectionDTO>> getConnections(String url){
        HttpHeaders headers = getHeaders();

        return rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                });
    }

    private String getUrlRequestParams(Long userId, Long roleId){
        return "?userId=" + userId + "&roleId=" + roleId;
    }

    private List<Long> verifyClassAttributes(String location, UserRoleConnection connection) {
        //get object
        UserRoleConnectionDTO connectionDTO = getConnection(location).getBody();

        //verify
        Assertions.assertNotNull(connectionDTO);
        Assertions.assertEquals(connectionDTO.getUserId(), connection.getUserId());
        Assertions.assertEquals(connectionDTO.getRoleId(), connection.getRoleId());

        return List.of(connectionDTO.getUserId(), connectionDTO.getRoleId());
    }

    private String addConnection(UserRoleConnection connection){
        //add
        ResponseEntity<UserRoleConnectionDTO> response = post(basePathUrl + "/add", connection, UserRoleConnectionDTO.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        //verify addition
        return response.getHeaders().getLocation() + "";
    }

    private UserRoleConnection getUserRoleConnection(Long userId, Long roleId){
        UserRoleConnection connection = new UserRoleConnection();
        connection.setUserId(userId);
        connection.setRoleId(roleId);
        return connection;
    }

    @Test
    public void getAll() {
        List<UserRoleConnectionDTO> connectionsDTO = getConnections(basePathUrl).getBody();
        Assertions.assertNotNull(connectionsDTO);
        Assertions.assertEquals(3, connectionsDTO.size());
    }

    @Test
    public void getByUserIdAndRoleId() {
        String url = basePathUrl + "/connection" + getUrlRequestParams(1L,1L);
        UserRoleConnectionDTO connectionDTO = getConnection(url).getBody();
        Assertions.assertNotNull(connectionDTO);
        Assertions.assertEquals(connectionDTO.getUserId(), 1L);
        Assertions.assertEquals(connectionDTO.getRoleId(), 1L);
    }

    @Test
    public void addAndDelete() {
        //add
        UserRoleConnection connection = getUserRoleConnection(1L,2L);
        String location = addConnection(connection);
        List<Long> ids = verifyClassAttributes(location, connection);

        //delete
        delete(basePathUrl + "/delete" + getUrlRequestParams(ids.get(0), ids.get(1)), null);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, getConnection(location).getStatusCode());
    }

    @Test
    public void deleteAllUserConnectionsById(){
        //add role admin to user
        UserRoleConnection connection = getUserRoleConnection(1L,2L);
        String location = addConnection(connection);
        verifyClassAttributes(location, connection);

        //get before delete
        List<UserRoleConnectionDTO> connectionsDTO = getConnections(basePathUrl).getBody();
        Assertions.assertNotNull(connectionsDTO);
        Assertions.assertEquals(2, connectionsDTO.stream().filter(connectionDTO -> (connectionDTO.getUserId() == 1L)).count());

        //delete
        delete(basePathUrl + "/delete/all/user/1", null);

        //verify
        connectionsDTO = getConnections(basePathUrl).getBody();
        Assertions.assertNotNull(connectionsDTO);
        Assertions.assertEquals(0, connectionsDTO.stream().filter(connectionDTO -> (connectionDTO.getUserId() == 1L)).count());

        //add role user to user
        connection = getUserRoleConnection(1L,1L);
        location = addConnection(connection);
        verifyClassAttributes(location, connection);
    }

    @Test
    public void deleteAllRoleConnectionsById(){

        //get before delete
        List<UserRoleConnectionDTO> connectionsDTO = getConnections(basePathUrl).getBody();
        Assertions.assertNotNull(connectionsDTO);
        Assertions.assertEquals(2, connectionsDTO.stream().filter(connectionDTO -> (connectionDTO.getRoleId() == 1L)).count());

        //delete
        delete(basePathUrl + "/delete/all/role/1", null);

        //verify
        connectionsDTO = getConnections(basePathUrl).getBody();
        Assertions.assertNotNull(connectionsDTO);
        Assertions.assertEquals(0, connectionsDTO.stream().filter(connectionDTO -> (connectionDTO.getRoleId() == 1L)).count());

        //add role user to user and admin
        for(long i=1; i<3; i++){
            UserRoleConnection connection = getUserRoleConnection(i,1L);
            String location = addConnection(connection);
            verifyClassAttributes(location, connection);
        }
    }
}
