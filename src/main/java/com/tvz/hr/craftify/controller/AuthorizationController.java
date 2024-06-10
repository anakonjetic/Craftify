package com.tvz.hr.craftify.controller;

import com.tvz.hr.craftify.model.RefreshToken;
import com.tvz.hr.craftify.model.Users;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.JwtService;
import com.tvz.hr.craftify.service.RefreshTokenService;
import com.tvz.hr.craftify.service.UserDetailsServiceImpl;
import com.tvz.hr.craftify.service.UsersService;
import com.tvz.hr.craftify.service.dto.LoginDTO;
import com.tvz.hr.craftify.service.dto.UserDTO;
import com.tvz.hr.craftify.service.dto.UsersGetDTO;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@AllArgsConstructor
@CrossOrigin(origins = {"http://test-craftify.vercel.app", "http://localhost:4200"})
public class AuthorizationController {
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private RefreshTokenService refreshTokenService;
    private UserDetailsServiceImpl userDetailsService;
    private UsersService usersService;

    @PostMapping("/login")
    public JwtResponseDTO authenticateAndGetToken(@RequestBody LoginDTO authRequestDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsernameOrEmail(), authRequestDTO.getPassword()));
        if(authentication.isAuthenticated()){
            UserDetails user = userDetailsService.loadUserByUsername(authRequestDTO.getUsernameOrEmail());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

            return JwtResponseDTO.builder()
                    .accessToken(jwtService.generateToken(user.getUsername()))
                    .token(refreshToken.getToken())
                    .user(usersService.getUserByUsername(user.getUsername()))
                    .build();
        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }
    }

    @PostMapping("/refreshToken")
    public JwtResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateToken(user.getUsername());
                    return JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequestDTO.getToken()).build();
                }).orElseThrow(() ->new RuntimeException("Refresh Token is not in Database!"));
    }

    @PostMapping("/logout/{userId}")
    public ResponseEntity<?> logout(@PathVariable Long userId) {
        System.out.println("Logout called with userId: " + userId); // Add logging
        try {
            refreshTokenService.removeToken(userId);
            System.out.println("Token removed for userId: " + userId); // Add logging
            return ResponseEntity.ok("Logged out successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Logout failed");
        }
    }

    // Endpoint for logging out a user by access token
    @PostMapping("/logout")
    public void logout(HttpServletRequest request){
        refreshTokenService.removeToken(request);
    }




    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class JwtResponseDTO {
        private String accessToken;
        private String token;
        private UserDTO user;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RefreshTokenRequestDTO {
        private String token;
    }
}
