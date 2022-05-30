package com.teethcare.service.impl.account;

import com.teethcare.model.entity.Role;
import com.teethcare.repository.RoleRepository;
import com.teethcare.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    final RoleRepository roleRepository;

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.getRoleByName(name);
    }
}
