package com.tetticket.ddd.application.security;

import com.tetticket.ddd.application.model.UsersDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration-in-ms}")
    private long accessTokenExpirationInMs;

    @Value("${jwt.refresh-token-expiration-in-ms}")
    private long refreshTokenExpirationInMs;

    @Value("${jwt.issuer}")
    private String issuer;

    public String extractUsername(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody();
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expirationDate = parseClaims(token).getExpiration();
            return expirationDate.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public String generateAccessToken(UsersDTO usersDTO) {
        return Jwts.builder()
                .setSubject(String.format("%s,%s", usersDTO.getId(), usersDTO.getUsername()))
                .setIssuer(issuer)
                .claim("roles", usersDTO.getRoles())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String generateRefreshToken(UsersDTO usersDTO) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", usersDTO.getId());
        claims.put("roles", usersDTO.getRoles());
        return doGenerateToken(claims, usersDTO.getUsername(), refreshTokenExpirationInMs);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject, long expirationInMs) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationInMs))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String authHeader) {
        String token = TokenExtractor.extractToken(authHeader);
        if (token == null) {
            return false;
        }
        try {
            logger.info("Token received: {}", token);
            Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            logger.error("Token validation failed", e);
            return false;
        }
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody();
    }

    public int getUserIdFromToken(String token) {
        return getClaimFromToken(token, claims -> Integer.parseInt(claims.getSubject().split(",")[0]));
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.getSubject().split(",")[1]);
    }
}