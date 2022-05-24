package com.teethcare.repository;

import com.teethcare.model.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Integer> {
    Manager getManagerByIdAndStatusIsNot(int id, String status);
}