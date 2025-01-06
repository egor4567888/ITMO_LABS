package com.example.config;
import com.example.util.TokenProvider;
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
import java.util.Collections;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
@Component
public class JwtAuthFilter extends GenericFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    private final TokenProvider tokenProvider;
 @Autowired
    public JwtAuthFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;
    String path = req.getRequestURI();

    logger.info("Processing request for path: {}", path);

    if (path.startsWith("/auth")) {
    logger.info("Skipping token check for path: {}", path);
    chain.doFilter(request, response);
    return;
}

        String authHeader = req.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);
            try {
                Claims claims = Jwts.parserBuilder()
        .setSigningKey(tokenProvider.getSecretKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    req.setAttribute("username", claims.getSubject());
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(claims.getSubject(), null, Collections.emptyList());
    SecurityContextHolder.getContext().setAuthentication(authentication);
    logger.info("Token valid for user: {}", claims.getSubject());
            } catch (Exception e) {
                logger.error("Invalid or expired token: {}"+token, e.getMessage());
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Неверный или просроченный токен");
                return;
            }
        } else {
            logger.warn("Authorization header missing or does not start with Bearer");
        }
        chain.doFilter(request, response);

    }
}