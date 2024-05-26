package com.project.shopapp.repository;

import com.project.shopapp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName (com.project.shopapp.enums.Role name);
}
