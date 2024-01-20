package com.cars;

import com.cars.api.dto.RoleDTO;
import com.cars.api.model.Role;
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
public class RoleAPITest extends BaseAPITest{

    private final String basePathUrl = "/api/v1/roles";

    private ResponseEntity<RoleDTO> getRole(String url){
        return get(url, RoleDTO.class);
    }

    private ResponseEntity<List<RoleDTO>> getRoles(String url){
        HttpHeaders headers = getHeaders();

        return rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<RoleDTO>>() {
                });
    }

    @Test
    public void getAll() {
        List<RoleDTO> rolesDTO = getRoles(basePathUrl).getBody();
        Assertions.assertNotNull(rolesDTO);
        Assertions.assertEquals(2, rolesDTO.size());
    }

    @Test
    public void getById() {
        RoleDTO roleDTO = getRole(basePathUrl + "/1").getBody();
        Assertions.assertNotNull(roleDTO);
        Assertions.assertEquals("ROLE_USER", roleDTO.getName());
    }

    @Test
    public void getByName() {
        RoleDTO roleDTO = getRole(basePathUrl + "/name/admin").getBody();
        Assertions.assertNotNull(roleDTO);
        Assertions.assertEquals("ROLE_ADMIN", roleDTO.getName());
    }

    @Test
    public void addUpdateAndDeleteById() {
        Role role = new Role();
        role.setName("ROLE_TEST");

        //add
        ResponseEntity<RoleDTO> response = post(basePathUrl + "/add", role, RoleDTO.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        //get
        String location = response.getHeaders().getLocation() + "";
        RoleDTO roleDTO = getRole(location).getBody();

        //verify addition
        Assertions.assertNotNull(roleDTO);
        Assertions.assertEquals(role.getName(), roleDTO.getName());

        //update
        role.setName("ROLE_TEST_UPDATE");
        response = put(basePathUrl + "/update/" + roleDTO.getId(), role, RoleDTO.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        //get
        roleDTO = getRole(location).getBody();

        //verify update
        Assertions.assertNotNull(roleDTO);
        Assertions.assertEquals(role.getName(), roleDTO.getName());

        //delete by id
        delete(basePathUrl + "/delete/" + roleDTO.getId(), null);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, getRole(location).getStatusCode());
    }

    @Test
    public void addAndDeleteByName() {
        Role role = new Role();
        role.setName("ROLE_TEST");

        //add
        ResponseEntity<RoleDTO> response = post(basePathUrl + "/add", role, RoleDTO.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        //get
        String location = response.getHeaders().getLocation() + "";
        RoleDTO roleDTO = getRole(location).getBody();

        //verify addition
        Assertions.assertNotNull(roleDTO);
        Assertions.assertEquals(role.getName(), roleDTO.getName());

        //delete by name
        delete(basePathUrl + "/delete/name/" + roleDTO.getName(), null);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, getRole(location).getStatusCode());
    }

}
