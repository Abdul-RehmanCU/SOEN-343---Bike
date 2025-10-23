package com.qwikride.service;

import com.qwikride.dto.RegistrationRequestDTO;
import com.qwikride.model.User;
import com.qwikride.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(RegistrationRequestDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setAddress(dto.getAddress());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setPaymentInfo(dto.getPaymentInfo());
        user.setRole(User.UserRole.RIDER);
        
        return userRepository.save(user);
    }
}