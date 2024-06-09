package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.RefreshToken;
import com.tvz.hr.craftify.model.Users;
import com.tvz.hr.craftify.repository.RefreshTokenRepository;
import com.tvz.hr.craftify.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    UsersService usersService;
    @Autowired
    JwtService jwtService;

    public RefreshToken createRefreshToken(String username){
        RefreshToken refreshToken = RefreshToken.builder()
                .user(usersRepository.findByUsername(username))
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(30*60)) //30 minutes
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please log in again.");
        }
        return token;
    }

    public void removeToken(Long id){
        Optional<RefreshToken> token = refreshTokenRepository.findByUser_id(id);
        token.ifPresent(refreshToken -> refreshTokenRepository.delete(refreshToken));
    }

    public void removeToken(HttpServletRequest request){
        Users user = usersService.getLoggedInUser();
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            String accessToken = header.substring(7);
            jwtService.revokeToken(accessToken);
        }
        Optional<RefreshToken> token = refreshTokenRepository.findByUser_id(user.getId());
        token.ifPresent(refreshToken -> refreshTokenRepository.delete(refreshToken));
    }
}
