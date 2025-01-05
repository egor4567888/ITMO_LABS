package com.example.controller;

import com.example.model.User;
import com.example.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.security.Keys;
import java.security.Key;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest req) {
        logger.info("Register attempt for username: {}", req.getUsername());
        if (userRepository.findByUsername(req.getUsername()) != null) {
            logger.warn("Registration failed: User {} already exists", req.getUsername());
            return ResponseEntity.badRequest().body("Пользователь уже существует");
        }
        
        User newUser = new User(req.getUsername(), passwordEncoder.encode(req.getPassword()));
        userRepository.save(newUser);
        logger.info("User {} registered successfully", req.getUsername());
        return ResponseEntity.ok("Пользователь зарегистрирован");
    }

   @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest req) {
        logger.info("Login attempt for username: {}", req.getUsername());
        User user = userRepository.findByUsername(req.getUsername());
        if (user == null) {
            logger.warn("Login failed: No such user {}", req.getUsername());
            return ResponseEntity.badRequest().body("Нет такого пользователя");
        }
        if (passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            String token = Jwts.builder()
            .setSubject(user.getUsername())
            .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
            .compact();
            logger.info("User {} logged in successfully", req.getUsername());
            return ResponseEntity.ok(token);
        } else {
            logger.warn("Login failed: Incorrect password for user {}", req.getUsername());
            return ResponseEntity.status(401).body("Неверный пароль");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        logger.info("Logout attempt");
        session.invalidate();
        logger.info("Session invalidated");
        return ResponseEntity.ok("Сессия завершена");
    }

    public static class AuthRequest {
        private String username;
        private String password;

        public AuthRequest() {}

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) { this.username = username; }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) { this.password = password; }
    }
}