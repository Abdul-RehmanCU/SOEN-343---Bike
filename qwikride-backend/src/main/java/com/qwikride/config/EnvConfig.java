package com.qwikride.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {

    private final Dotenv dotenv = Dotenv.load();

    @Bean
    public String supabaseUrl() {
        return dotenv.get("SUPABASE_URL");
    }

    @Bean
    public String supabaseServiceRoleKey() {
        return dotenv.get("SUPABASE_SERVICE_ROLE_KEY");
    }

    @Bean
    public String supabaseAnonKey() {
        return dotenv.get("SUPABASE_ANON_KEY");
    }
}