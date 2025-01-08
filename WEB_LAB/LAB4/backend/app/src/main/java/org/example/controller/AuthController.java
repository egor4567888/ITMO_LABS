package com.example.controller;

import com.example.dto.ResponseDTO;
import com.example.dto.UserDTO;
import com.example.service.AuthService;
import com.example.service.AuthTokens;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<String>> register(@RequestBody UserDTO req) {
        ResponseDTO<String> result = authService.register(req);
        if ("Пользователь уже существует".equals(result.getMessage())) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

     @PostMapping("/login")
    public ResponseEntity<ResponseDTO<AuthTokens>> login(@RequestBody AuthRequest req) {
        ResponseDTO<AuthTokens> result = authService.login(req.getUsername(), req.getPassword());
        if ("Нет такого пользователя".equals(result.getMessage())) {
            return ResponseEntity.badRequest().body(result);
        }
        if ("Неверный пароль".equals(result.getMessage())) {
            return ResponseEntity.status(401).body(result);
        }
        return ResponseEntity.ok(result);
    }
    @GetMapping("/logout")
    public ResponseEntity<ResponseDTO<String>> logout(HttpSession session) {
        ResponseDTO<String> result = authService.logout(session);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/refresh")
    public ResponseEntity<ResponseDTO<AuthTokens>> refresh(@RequestBody RefreshRequest req) {
        ResponseDTO<AuthTokens> result = authService.refresh(req.getRefreshToken());
        if ("Refresh token invalid or expired".equals(result.getMessage())) {
            return ResponseEntity.status(401).body(result);
        }
        return ResponseEntity.ok(result);
    }

    public static class AuthRequest {
        private String username;
        private String password;

        public AuthRequest() {}

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    public static class RefreshRequest {
        private String refreshToken;
        public RefreshRequest() {}
        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    }
}