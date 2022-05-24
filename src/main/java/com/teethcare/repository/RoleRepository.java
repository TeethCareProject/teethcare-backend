package com.teethcare.repository;

import com.teethcare.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role getRoleByName(String name);
}