package com.qwikride.config;

import com.qwikride.model.User;
import com.qwikride.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("operator")) {
            User operator = new User();
            operator.setFullName("System Operator");
            operator.setAddress("QwikRide HQ");
            operator.setEmail("operator@qwikride.com");
            operator.setUsername("operator");
            operator.setPasswordHash(passwordEncoder.encode("operator123"));
            operator.setPaymentInfo("N/A");
            operator.setRole(User.UserRole.OPERATOR);
            
            userRepository.save(operator);
            log.info("Operator account seeded successfully");
        }
    }
}