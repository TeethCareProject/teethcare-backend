package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Role;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.config.security.UserDetailsImpl;
import com.teethcare.mapper.ClinicMapper;
import com.teethcare.mapper.LoginMapper;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.request.LoginRequest;
import com.teethcare.model.request.RefreshTokenRequest;
import com.teethcare.model.response.ClinicLoginResponse;
import com.teethcare.model.response.LoginResponse;
import com.teethcare.model.response.RefreshTokeResponse;
import com.teethcare.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final AccountService accountService;
    private final LoginMapper loginMapper;
    private final ClinicService clinicService;
    private final ManagerService managerService;
    private final DentistService dentistService;
    private final CSService csService;
    private final ClinicMapper clinicMapper;

    @PostMapping(path = EndpointConstant.Authentication.LOGIN_ENDPOINT)
    public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication authentication = authManager.authenticate(authenticationToken);
        if (authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtTokenUtil.generateToken(authentication);
            String refreshToken = jwtTokenUtil.generateRefreshToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining());
            LoginResponse response = loginMapper.mapUserDetailsImplToLoginResponse(userDetails);
            response.setRoleName(role);

            Clinic clinic = null;
            if ((Role.DENTIST.name()).equals(role)) {
                clinic = dentistService.findById(response.getId()).getClinic();
            } else if ((Role.CUSTOMER_SERVICE.name()).equals(role)) {
                clinic = csService.findById(response.getId()).getClinic();
            } else if ((Role.MANAGER.name().equals(role))) {
                clinic = clinicService.getClinicByManager(managerService.findById(response.getId()));
            }
            ClinicLoginResponse clinicResponse = clinicMapper.mapClinicToClinicLoginResponse(clinic);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setClinic(clinicResponse);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(path = EndpointConstant.Authentication.REFRESH_TOKEN_ENDPOINT)
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest tokenRequest) {
        if (!jwtTokenUtil.validateToken(tokenRequest.getRefreshToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
        String username = jwtTokenUtil.getUsernameFromJwt(tokenRequest.getRefreshToken());
        Account account = accountService.getAccountByUsername(username);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token claim is invalid");
        }
        String newToken = jwtTokenUtil.generateToken(account.getUsername());
        String newRefreshToken = jwtTokenUtil.generateRefreshToken(account.getUsername());
        RefreshTokeResponse response = new RefreshTokeResponse(newToken, newRefreshToken);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


}
