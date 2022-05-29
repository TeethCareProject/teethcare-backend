package com.teethcare.mapper;

import com.teethcare.config.security.UserDetailsImpl;
import com.teethcare.model.response.LoginResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")

public interface LoginMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    LoginResponse mapUserDetailsImplToLoginResponse (UserDetailsImpl userDetails);
}
