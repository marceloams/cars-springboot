package com.cars.api.repository;

import com.cars.api.model.UserRoleConnection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleConnectionRepository extends JpaRepository<UserRoleConnection, Long> {
    UserRoleConnection findByUserIdAndRoleId(Long userId, Long roleId);

    int countByUserId(Long userId);

    void deleteByUserId(Long userId);

    int countByRoleId(Long id);

    void deleteByRoleId(Long id);
}
