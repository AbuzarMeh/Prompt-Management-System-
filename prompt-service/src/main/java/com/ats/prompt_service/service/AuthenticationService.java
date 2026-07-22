package com.ats.prompt_service.service;

import com.ats.prompt_service.config.AuthenticationProperties;
import com.ats.prompt_service.exception.InvalidCredentialsException;
import com.ats.prompt_service.dto.request.LoginRequest;
import com.ats.prompt_service.dto.response.LoginResponse;
import com.ats.prompt_service.security.JwtService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationProperties authenticationProperties;

    public AuthenticationService(
            JwtService jwtService,
            AuthenticationProperties authenticationProperties
    ) {
        this.jwtService = jwtService;
        this.authenticationProperties = authenticationProperties;
    }

    public LoginResponse authenticate(LoginRequest request) {


        if (!authenticationProperties.getUsername().equals(request.getUsername())) {
            throw new InvalidCredentialsException();
        }

        if (!authenticationProperties.getPassword().equals(request.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(request.getUsername());

        

        return new LoginResponse(token);
    }

}