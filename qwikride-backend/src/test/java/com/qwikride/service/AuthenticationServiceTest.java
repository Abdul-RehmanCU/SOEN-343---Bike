package com.qwikride.service;

import com.qwikride.dto.LoginRequestDTO;
import com.qwikride.dto.LoginResponseDTO;
import com.qwikride.model.User;
import com.qwikride.repository.UserRepository;
import com.qwikride.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    // Use a real JwtUtil instance in tests to avoid inline-mock instrumentation
    // issues
    private JwtUtil jwtUtil;

    @Mock
    private SupabaseService supabaseService;

    private AuthenticationService authenticationService;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        // initialize a real JwtUtil and construct the service manually
        this.jwtUtil = new JwtUtil();
        this.authenticationService = new AuthenticationService(userRepository, passwordEncoder, jwtUtil,supabaseService);
    }

    @Test
    void authenticate_ValidCredentials_ReturnsToken() {
        // Given
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername("johndoe");
        dto.setPassword("password123");

        User user = new User();
        user.setUsername("johndoe");
        user.setFullName("John Doe");
        user.setPasswordHash("hashedPassword");
        user.setRole(User.UserRole.RIDER);

        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);

        // When
        LoginResponseDTO response = authenticationService.authenticate(dto);

        // Then
        assertNotNull(response);
        assertNotNull(response.getToken());
        // Validate token produced by real JwtUtil
        assertTrue(jwtUtil.validateToken(response.getToken(), "johndoe"));
        assertEquals("johndoe", response.getUsername());
        assertEquals("John Doe", response.getFullName());
        assertEquals("RIDER", response.getRole());
    }

    @Test
    void authenticate_InvalidUsername_ThrowsException() {
        // Given
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername("nonexistent");
        dto.setPassword("password123");

        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> authenticationService.authenticate(dto));
    }

    @Test
    void authenticate_InvalidPassword_ThrowsException() {
        // Given
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername("johndoe");
        dto.setPassword("wrongpassword");

        User user = new User();
        user.setPasswordHash("hashedPassword");

        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "hashedPassword")).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> authenticationService.authenticate(dto));
    }
}