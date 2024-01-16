package com.cars.api.repository;

import com.cars.api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

    @Query(value =
            "SELECT u.name " +
            "FROM role r " +
            "INNER JOIN user_roles ur ON r.id = ur.role_id " +
            "INNER JOIN users u ON u.id = ur.user_id "+
            "WHERE r.name = :roleName",
            nativeQuery = true
    )
    List<String> findUsernamesByRole(@Param("roleName")String name);
}
