package com.teethcare.repository;

import com.teethcare.config.model.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WardRepository extends JpaRepository<Ward, Integer> {
    Ward getWardById(int id);
}