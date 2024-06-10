package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.utilities.exceptions.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    private static final String SECRET = "D1E2F3A4B5C6D7E8F9E0D1C2B3A4F5E6D7C8B9A0E1F2D3C4B5A6D7E8F9E0D1C2";

    @InjectMocks
    public JwtService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void extractUsername() {
        String token = jwtService.generateToken("testUser");

        String username = jwtService.extractUsername(token);

        assertEquals("testUser", username);
    }

    @Test
    void extractExpiration() {
        String token = jwtService.generateToken("testUser");

        Date expiration = jwtService.extractExpiration(token);

        assertNotNull(expiration);
    }

    @Test
    void extractClaim() {
        String token = jwtService.generateToken("testUser");

        Claims claims = jwtService.extractAllClaims(token);

        assertNotNull(claims);
        assertEquals("testUser", claims.getSubject());
    }

    @Test
    void generateToken() {
        String username = "testUser";

        String token = jwtService.generateToken(username);

        assertNotNull(token);
    }

    @Test
    void isTokenExpired() {
        String token = jwtService.generateToken("testUser");

        boolean expired = jwtService.isTokenExpired(token);

        assertFalse(expired);
    }

    @Test
    void validateToken_Valid() {
        String token = jwtService.generateToken("testUser");

        boolean valid = jwtService.validateToken(token, new TestUserDetails("testUser"));

        assertTrue(valid);
    }

    @Test
    void validateToken_Expired() {
        String token = jwtService.generateToken("testUser");
        jwtService.generateToken("testUser");

        assertTrue(jwtService.validateToken(token, new TestUserDetails("testUser")));
    }

    @Test
    void revokeToken() {
        String token = jwtService.generateToken("testUser");

        jwtService.revokeToken(token);

        assertTrue(jwtService.isTokenRevoked(token));
    }

    private static class TestUserDetails implements org.springframework.security.core.userdetails.UserDetails {

        private final String username;

        public TestUserDetails(String username) {
            this.username = username;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public java.util.Collection<org.springframework.security.core.GrantedAuthority> getAuthorities() {
            return null;
        }
    }
}
