package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.config.security.UserDetailsImpl;
import com.teethcare.model.entity.Account;
import com.teethcare.model.request.LoginRequest;
import com.teethcare.model.request.RefreshTokenRequest;
import com.teethcare.model.response.CustomErrorResponse;
import com.teethcare.model.response.LoginResponse;
import com.teethcare.model.response.RefreshTokeResponse;
import com.teethcare.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final AccountService accountService;

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

            LoginResponse response = new LoginResponse(
                    userDetails.getUsername(),
                    role,
                    userDetails.getFirstName(),
                    userDetails.getLastName(),
                    userDetails.getAvatarImage(),
                    userDetails.getDateOfBirth(),
                    userDetails.getEmail(),
                    userDetails.getPhone(),
                    userDetails.getGender(),
                    userDetails.getStatus(),
                    jwt,
                    refreshToken
            );
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List errors = new ArrayList<String>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(fieldName + " " + errorMessage);
        });
        CustomErrorResponse customErrorResponse = new CustomErrorResponse(
                new Timestamp(System.currentTimeMillis()),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.toString(),
                errors
        );
        return new ResponseEntity<>(customErrorResponse, HttpStatus.BAD_REQUEST);

    }


}
