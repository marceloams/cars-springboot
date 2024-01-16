package com.cars;

import com.cars.api.dto.RoleDTO;
import com.cars.api.exception.ObjectNotFoundException;
import com.cars.api.model.Role;
import com.cars.api.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class RoleServiceTest {

    @Autowired
    RoleService roleService;

    @Test
    public void listRoles(){
        List<RoleDTO> roles = roleService.getAll();

        Assertions.assertEquals(2, roles.size());
    }

    @Test
    public void getByName(){
        RoleDTO roleDTO = roleService.getByName("admin");
        Assertions.assertEquals("ROLE_ADMIN", roleDTO.getName());

        roleDTO = roleService.getByName("ROLE_NONE_EXISTENT");
        Assertions.assertNull(roleDTO);
    }

    @Test
    public void getById(){
        RoleDTO roleDTO = roleService.getById(1L);
        Assertions.assertEquals(1L, roleDTO.getId());
    }

    @Test
    public void insert(){
        Role role = new Role();
        role.setName("ROLE_NEW");

        //adding object
        RoleDTO roleDTO = roleService.insert(role);
        Assertions.assertNotNull(roleDTO);

        Long id = roleDTO.getId();
        Assertions.assertNotNull(id);

        //getting object by id
        roleDTO = roleService.getById(id);
        Assertions.assertNotNull(roleDTO);

        Assertions.assertEquals(role.getName(), roleDTO.getName());

        //delete object by id
        roleService.deleteById(id);
        try{
            Assertions.assertNull(roleService.getById(id));
            Assertions.fail("Role not deleted!");
        }catch (ObjectNotFoundException e){
            //ok
        }
    }

    @Test
    public void update(){
        Role role = new Role();
        role.setName("ROLE_NEW");

        //adding object
        RoleDTO roleDTO = roleService.insert(role);
        Assertions.assertNotNull(roleDTO);

        String name = roleDTO.getName();
        Assertions.assertNotNull(name);

        //getting object by name
        roleDTO = roleService.getByName(name);
        Assertions.assertNotNull(roleDTO);

        Assertions.assertEquals(role.getName(), roleDTO.getName());

        //updating object
        role.setName("ROLE_NEW_UPDATED");

        Long id = roleDTO.getId();
        Assertions.assertNotNull(id);

        roleDTO = roleService.updateById(id, role);
        Assertions.assertNotNull(roleDTO);

        name = roleDTO.getName();
        Assertions.assertNotNull(name);

        roleDTO = roleService.getByName(name);
        Assertions.assertNotNull(roleDTO);

        Assertions.assertEquals(role.getName(), roleDTO.getName());

        //delete object
        roleService.deleteByName(name);
        Assertions.assertNull(roleService.getByName(name), "Role not deleted!");
    }

    @Test
    public void deleteResponses(){
        //trying to delete role assigned to a user
        Assertions.assertEquals(RoleService.StatusAnswer.ASSIGNED, roleService.deleteByName("ROLE_USER"));
        Assertions.assertEquals(RoleService.StatusAnswer.ASSIGNED, roleService.deleteById(1L));

        //trying to delete role none existent
        Assertions.assertEquals(RoleService.StatusAnswer.NOT_FOUND, roleService.deleteByName("ROLE_NONE_EXISTENT"));
        Assertions.assertThrows(ObjectNotFoundException.class, () -> roleService.deleteById(3L));
    }
}
