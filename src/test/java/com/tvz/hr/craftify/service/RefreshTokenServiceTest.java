package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.RefreshToken;
import com.tvz.hr.craftify.model.Users;
import com.tvz.hr.craftify.repository.RefreshTokenRepository;
import com.tvz.hr.craftify.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private UsersService usersService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRefreshToken_Success() {
        String username = "testUser";
        Users user = new Users();
        user.setUsername(username);
        when(usersRepository.findByUsername(username)).thenReturn(user);
        when(refreshTokenRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(username);

        assertNotNull(refreshToken);
        assertEquals(username, refreshToken.getUser().getUsername());
        assertNotNull(refreshToken.getToken());
        assertNotNull(refreshToken.getExpiryDate());
    }

    @Test
    void findByToken_Success() {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken();
        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(refreshToken));

        Optional<RefreshToken> foundToken = refreshTokenService.findByToken(token);

        assertTrue(foundToken.isPresent());
        assertEquals(refreshToken, foundToken.get());
    }

    @Test
    void verifyExpiration_NotExpired() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(Instant.now().plusSeconds(60));

        assertDoesNotThrow(() -> refreshTokenService.verifyExpiration(refreshToken));
    }

    @Test
    void verifyExpiration_Expired() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(Instant.now().minusSeconds(60));

        assertThrows(RuntimeException.class, () -> refreshTokenService.verifyExpiration(refreshToken));
    }

    @Test
    void removeTokenById_Success() {
        Long id = 1L;
        RefreshToken refreshToken = new RefreshToken();
        when(refreshTokenRepository.findByUser_id(id)).thenReturn(Optional.of(refreshToken));

        refreshTokenService.removeToken(id);

        verify(refreshTokenRepository, times(1)).delete(refreshToken);
    }

    @Test
    void removeTokenByRequest_Success() {
        Users user = new Users();
        user.setId(1L);
        String token = "mockAccessToken";
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(usersService.getLoggedInUser()).thenReturn(user);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        when(refreshTokenRepository.findByUser_id(user.getId())).thenReturn(Optional.of(new RefreshToken()));

        assertDoesNotThrow(() -> refreshTokenService.removeToken(request));
        verify(jwtService, times(1)).revokeToken(token);
        verify(refreshTokenRepository, times(1)).delete(any());
    }
}
