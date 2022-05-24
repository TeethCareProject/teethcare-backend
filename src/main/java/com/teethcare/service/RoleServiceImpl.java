package com.teethcare.service;

import com.teethcare.config.model.entity.Role;
import com.teethcare.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{
    final RoleRepository roleRepository;
    @Override
    public Role getRoleByName(String name) {
        return roleRepository.getRoleByName(name);
    }
}
