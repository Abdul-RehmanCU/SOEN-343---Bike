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
    private final SupabaseService supabaseService;

    public LoginResponseDTO authenticate(LoginRequestDTO dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        boolean authenticated = false;
        try {
            // Try Supabase sign-in using stored email
            supabaseService.signIn(user.getEmail(), dto.getPassword());
            authenticated = true;
        } catch (Exception ex) {
            // Fallback to local bcrypt check
            authenticated = passwordEncoder.matches(dto.getPassword(), user.getPasswordHash());
        }

        if (!authenticated) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        return new LoginResponseDTO(token, user.getUsername(), user.getFullName(), user.getRole().name());
    }

    // ✅ New registration method
    public void register(RegistrationRequestDTO dto) {
        // 1. Prevent duplicate emails or usernames
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }

        try {
            // 2. Create user in Supabase (for consistency with your login flow)
            supabaseService.createUser(dto.getEmail(), dto.getPassword());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to register user in Supabase: " + e.getMessage());
        }

        // 3. Save in local database
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
