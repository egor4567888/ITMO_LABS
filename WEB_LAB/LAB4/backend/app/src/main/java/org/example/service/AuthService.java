package com.example.service;

import com.example.dto.ResponseDTO;
import com.example.dto.UserDTO;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.util.TokenProvider;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class AuthService {
   private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public ResponseDTO<String> register(UserDTO req) {
        logger.info("Register attempt for username: {}", req.getUsername());
        if (userRepository.findByUsername(req.getUsername()) != null) {
            logger.warn("Registration failed: User {} already exists", req.getUsername());
            return new ResponseDTO<>(null, "Пользователь уже существует");
        }
        User newUser = new User(req.getUsername(), passwordEncoder.encode(req.getPassword()));
        userRepository.save(newUser);
        logger.info("User {} registered successfully", req.getUsername());
        return new ResponseDTO<>(null, "Пользователь зарегистрирован");
    }

    public ResponseDTO<AuthTokens> login(String username, String password) {
        logger.info("Login attempt for username: {}", username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return new ResponseDTO<>(null, "Нет такого пользователя");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return new ResponseDTO<>(null, "Неверный пароль");
        }
        String access = tokenProvider.generateAccessToken(username);
        String refresh = tokenProvider.generateRefreshToken(username);
        return new ResponseDTO<>(new AuthTokens(access, refresh), null);
    }

    public ResponseDTO<AuthTokens> refresh(String refreshToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(tokenProvider.getSecretKey())
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();
            String username = claims.getSubject();
            String newAccess = tokenProvider.generateAccessToken(username);
            String newRefresh = tokenProvider.generateRefreshToken(username);
            return new ResponseDTO<>(new AuthTokens(newAccess, newRefresh), null);
        } catch (Exception e) {
            logger.error("Refresh token invalid or expired", e.getMessage());
            return new ResponseDTO<>(null, "Refresh token invalid or expired");
        }
    }

    public ResponseDTO<String> logout(HttpSession session) {
        logger.info("Logout attempt");
        session.invalidate();
        logger.info("Session invalidated");
        return new ResponseDTO<>(null, "Сессия завершена");
    }
}