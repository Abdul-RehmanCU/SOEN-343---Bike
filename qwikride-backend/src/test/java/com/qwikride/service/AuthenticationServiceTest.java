package com.qwikride.service;

import com.qwikride.dto.LoginRequestDTO;
import com.qwikride.dto.LoginResponseDTO;
import com.qwikride.model.User;
import com.qwikride.repository.UserRepository;
import com.qwikride.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
    
    @Mock
    private JwtUtil jwtUtil;
    
    @InjectMocks
    private AuthenticationService authenticationService;
    
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
        when(jwtUtil.generateToken("johndoe", "RIDER")).thenReturn("mock-jwt-token");
        
        // When
        LoginResponseDTO response = authenticationService.authenticate(dto);
        
        // Then
        assertNotNull(response);
        assertEquals("mock-jwt-token", response.getToken());
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