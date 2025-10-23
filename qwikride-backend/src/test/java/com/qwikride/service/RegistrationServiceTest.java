package com.qwikride.service;

import com.qwikride.dto.RegistrationRequestDTO;
import com.qwikride.model.User;
import com.qwikride.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private RegistrationService registrationService;
    
    @Test
    void register_ValidRequest_ReturnsUser() {
        // Given
        RegistrationRequestDTO dto = new RegistrationRequestDTO();
        dto.setFullName("John Doe");
        dto.setEmail("john@example.com");
        dto.setUsername("johndoe");
        dto.setPassword("password123");
        dto.setAddress("123 Main St");
        
        when(userRepository.existsByUsername("johndoe")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        User result = registrationService.register(dto);
        
        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getFullName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("johndoe", result.getUsername());
        assertEquals(User.UserRole.RIDER, result.getRole());
    }
    
    @Test
    void register_DuplicateUsername_ThrowsException() {
        // Given
        RegistrationRequestDTO dto = new RegistrationRequestDTO();
        dto.setUsername("existinguser");
        
        when(userRepository.existsByUsername("existinguser")).thenReturn(true);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> registrationService.register(dto));
    }
}