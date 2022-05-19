package com.teethcare.controller;

import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.config.security.UserDetailsImpl;
import com.teethcare.model.entity.Account;
import com.teethcare.model.request.LoginRequest;
import com.teethcare.model.request.RefreshTokenRequest;
import com.teethcare.model.response.LoginResponse;
import com.teethcare.model.response.RefreshTokeResponse;
import com.teethcare.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final AccountService accountService;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest request){
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication authentication = authManager.authenticate(authenticationToken);
        if (authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtTokenUtil.generateToken(authentication);
            String refreshToken = jwtTokenUtil.generateRefreshToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining());

            LoginResponse response = new LoginResponse(
                    userDetails.getUsername(),
                    userDetails.getPassword(),
                    role,
                    userDetails.getFirstName(),
                    userDetails.getLastName(),
                    userDetails.isGender(),
                    userDetails.getAvatarImage(),
                    userDetails.getDateOfBirth(),
                    userDetails.isStatus(),
                    jwt,
                    refreshToken

            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest tokenRequest){
        if(!jwtTokenUtil.validateToken(tokenRequest.getRefreshToken())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
        String username = jwtTokenUtil.getUsernameFromJwt(tokenRequest.getRefreshToken());
        Account account = accountService.getAccountByUsername(username);
        if(account == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token claim is invalid");
        }
        String newToken = jwtTokenUtil.generateToken(account.getUsername());
        String newRefreshToken = jwtTokenUtil.generateRefreshToken(account.getUsername());
        RefreshTokeResponse response = new RefreshTokeResponse(newToken, newRefreshToken);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


    
}
