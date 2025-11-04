package com.qwikride.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.qwikride.config.EnvConfig;

import java.util.Map;

@Service
public class SupabaseService {
    private final RestTemplate rest = new RestTemplate();

    private final String baseUrl;
    private final String serviceRoleKey;
    private final String anonKey;

    // Inject from EnvConfig
    public SupabaseService(String supabaseUrl, String supabaseServiceRoleKey, String supabaseAnonKey) {
        this.baseUrl = supabaseUrl;
        this.serviceRoleKey = supabaseServiceRoleKey;
        this.anonKey = supabaseAnonKey;
    }

    public void createUser(String email, String password) {
        String url = baseUrl + "/auth/v1/admin/users";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(serviceRoleKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> body = Map.of("email", email, "password", password);
        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, headers);
        ResponseEntity<String> resp = rest.postForEntity(url, req, String.class);
        if (!resp.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("Supabase create user failed: " + resp.getBody());
        }
    }

    public Map<String, Object> signIn(String email, String password) {
        String url = baseUrl + "/auth/v1/token?grant_type=password";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(anonKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> body = Map.of("email", email, "password", password);
        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, headers);
        ResponseEntity<Map> resp = rest.postForEntity(url, req, Map.class);
        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return resp.getBody();
    }

    public Map<String, Object> getUserByToken(String accessToken) {
        String url = baseUrl + "/auth/v1/user";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> req = new HttpEntity<>(headers);
        ResponseEntity<Map> resp = rest.exchange(url, HttpMethod.GET, req, Map.class);
        return resp.getBody();
    }
}
