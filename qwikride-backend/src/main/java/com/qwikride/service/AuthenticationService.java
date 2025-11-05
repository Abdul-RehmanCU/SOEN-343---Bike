package com.qwikride.service;

import com.qwikride.dto.LoginRequestDTO;
import com.qwikride.dto.LoginResponseDTO;
import com.qwikride.model.User;
import com.qwikride.repository.UserRepository;
import com.qwikride.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponseDTO authenticate(LoginRequestDTO dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
        
        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        
        return new LoginResponseDTO(
                token,
                user.getUsername(),
                user.getFullName(),
                user.getRole().name(),
                user.getId()
        );
    }
}