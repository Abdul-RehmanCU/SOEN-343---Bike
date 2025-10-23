package com.qwikride.controller;

import com.qwikride.dto.RegistrationRequestDTO;
import com.qwikride.model.User;
import com.qwikride.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegistrationRequestDTO dto) {
        try {
            User user = registrationService.register(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}