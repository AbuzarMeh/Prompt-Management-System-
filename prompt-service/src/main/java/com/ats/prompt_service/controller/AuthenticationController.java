package com.ats.prompt_service.controller;

import com.ats.prompt_service.dto.request.LoginRequest;
import com.ats.prompt_service.dto.response.LoginResponse;
import com.ats.prompt_service.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        LoginResponse response = authenticationService.authenticate(request);

        return ResponseEntity.ok(response);
    }
}