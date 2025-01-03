package com.example.controller;

import com.example.model.User;
import com.example.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest req) {
        if (userRepository.findByUsername(req.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Пользователь уже существует");
        }
        // Используем PasswordEncoder для хеширования пароля
        User newUser = new User(req.getUsername(), passwordEncoder.encode(req.getPassword()));
        userRepository.save(newUser);
        return ResponseEntity.ok("Пользователь зарегистрирован");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest req, HttpSession session) {
        System.out.println("Login attempt: username=" + req.getUsername() + ", password=" + req.getPassword());
        User user = userRepository.findByUsername(req.getUsername());
        if (user == null) {
            System.out.println("User not found: " + req.getUsername());
            return ResponseEntity.badRequest().body("Нет такого пользователя");
        }
        // Используем PasswordEncoder для проверки пароля
        boolean passwordMatch = passwordEncoder.matches(req.getPassword(), user.getPassword());
        System.out.println("Password match: " + passwordMatch);
        if (passwordMatch) {
            session.setAttribute("username", user.getUsername());
            return ResponseEntity.ok("Успешный вход");
        } else {
            return ResponseEntity.status(401).body("Неверный пароль");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
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