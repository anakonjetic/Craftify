package com.tvz.hr.craftify.service;


import com.tvz.hr.craftify.model.Users;
import com.tvz.hr.craftify.utilities.exceptions.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

@Component
@ComponentScan("com.tvz.hr.craftify.service")
public class JwtService {
    private ConcurrentMap<String, Boolean> revokedTokens = new ConcurrentHashMap<>();
    public static final String SECRET = "D1E2F3A4B5C6D7E8F9E0D1C2B3A4F5E6D7C8B9A0E1F2D3C4B5A6D7E8F9E0D1C2";
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException | MalformedJwtException e) {
            throw new MalformedJwtException("Invalid JWT token", e);
        } catch (TokenExpiredException e) {
            throw new RuntimeException("JWT token is expired", e);
        } catch (Exception e) {
            throw new RuntimeException("Error extracting claims from JWT", e);
        }
    }

    public String extractUsername(String token) { return extractClaim(token, Claims::getSubject); }
    public Date extractExpiration(String token) { return extractClaim(token, Claims::getExpiration); }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+10*60*1000)) //10 minutes
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    public Boolean validateToken(String token, UserDetails user) {
        if (isTokenExpired(token)) {
            throw new TokenExpiredException("JWT token is expired");
        }
        final String username = extractUsername(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    public void revokeToken(String token) {
        revokedTokens.put(token, true);
    }

    public boolean isTokenRevoked(String token) {
        return revokedTokens.getOrDefault(token, false);
    }
}
