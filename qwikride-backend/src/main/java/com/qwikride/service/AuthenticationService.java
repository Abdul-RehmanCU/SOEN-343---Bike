package com.qwikride.service;

import com.qwikride.dto.LoginRequestDTO;
import com.qwikride.dto.LoginResponseDTO;
import com.qwikride.dto.RegistrationRequestDTO;
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

    public void register(RegistrationRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }

        User user = new User();
        user.setFullName(dto.getFullName());
        user.setAddress(dto.getAddress());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setPaymentInfo(dto.getPaymentInfo());
        user.setRole(User.UserRole.RIDER); 

        userRepository.save(user);
    }
}
