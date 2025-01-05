package com.example.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.Key;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
@Component
public class JwtAuthFilter extends GenericFilter {
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    private final String secret = "secretKey";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getRequestURI();
        logger.debug("Processing request for path: {}", path);

        if (path.startsWith("/auth/")) {
            logger.debug("Path starts with /auth/, skipping JWT validation");
            chain.doFilter(request, response);
            return;
        }

        String authHeader = req.getHeader("Authorization");
         if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(SECRET_KEY)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
                req.setAttribute("username", claims.getSubject());
                logger.debug("Token valid for user: {}", claims.getSubject());
            } catch (Exception e) {
                logger.error("Invalid or expired token: {}", e.getMessage());
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Неверный или просроченный токен");
                return;
            }
        } else {
            logger.warn("Authorization header missing or does not start with Bearer");
        }
        chain.doFilter(request, response);
    }
}