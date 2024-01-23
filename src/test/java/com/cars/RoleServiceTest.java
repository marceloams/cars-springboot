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

        roleDTO = roleService.getByName("ROLE_NON_EXISTENT");
        Assertions.assertNull(roleDTO);
    }

    @Test
    public void getById(){
        RoleDTO roleDTO = roleService.getById(1L);
        Assertions.assertEquals(1L, roleDTO.getId());
    }

    private Long insert(){
        //insert
        Role role = new Role();
        role.setName("ROLE_NEW");
        RoleDTO roleDTO = roleService.insert(role);
        Assertions.assertNotNull(roleDTO);

        //get id
        Long id = roleDTO.getId();
        Assertions.assertNotNull(id);

        //get by id
        roleDTO = roleService.getById(id);
        Assertions.assertNotNull(roleDTO);

        //verify
        Assertions.assertEquals(role.getName(), roleDTO.getName());

        return id;
    }

    @Test
    public void insertUpdateAndDeleteById() {

        //INSERT
        Long id = insert();

        //UPDATE
        Role role = new Role();
        role.setName("ROLE_NEW_UPDATED");
        RoleDTO roleDTO = roleService.updateById(id, role);
        Assertions.assertNotNull(roleDTO);

        //get name
        String name = roleDTO.getName();
        Assertions.assertNotNull(name);

        //get by name
        roleDTO = roleService.getByName(name);
        Assertions.assertNotNull(roleDTO);

        //verify
        Assertions.assertEquals(role.getName(), roleDTO.getName());

        //DELETE by id
        roleService.deleteById(id);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> roleService.deleteById(id));
    }

    @Test
    public void insertAndDeleteByName() {
        //INSERT
        Long id = insert();

        //get name
        RoleDTO roleDTO = roleService.getById(id);
        Assertions.assertNotNull(roleDTO);
        String name = roleDTO.getName();
        Assertions.assertNotNull(name);

        //delete object
        roleService.deleteByName(name);
        Assertions.assertNull(roleService.getByName(name), "Role not deleted!");
    }

    @Test
    public void deleteResponses(){
        //delete role assigned to a user
        Assertions.assertEquals(RoleService.StatusAnswer.ASSIGNED, roleService.deleteByName("ROLE_USER"));
        Assertions.assertEquals(RoleService.StatusAnswer.ASSIGNED, roleService.deleteById(1L));

        //delete role non-existent
        Assertions.assertEquals(RoleService.StatusAnswer.NOT_FOUND, roleService.deleteByName("ROLE_NON_EXISTENT"));
        Assertions.assertThrows(ObjectNotFoundException.class, () -> roleService.deleteById(3L));
    }
}
