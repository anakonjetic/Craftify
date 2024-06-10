package com.tvz.hr.craftify.controller;

import com.tvz.hr.craftify.config.SecurityConfiguration;
import com.tvz.hr.craftify.filter.JwtAuthFilter;
import com.tvz.hr.craftify.model.RefreshToken;
import com.tvz.hr.craftify.model.Users;
import com.tvz.hr.craftify.service.JwtService;
import com.tvz.hr.craftify.service.RefreshTokenService;
import com.tvz.hr.craftify.service.UserDetailsServiceImpl;
import com.tvz.hr.craftify.service.UsersService;
import com.tvz.hr.craftify.service.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorizationController.class)
@Import(SecurityConfiguration.class)
public class AuthorizationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UsersService usersService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private RefreshTokenService refreshTokenService;

    @MockBean
    private UserDetailsService userDetailsServiceS;

    @MockBean
    private AuthorizationController authorizationController;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    public void authenticateAndGetToken_ValidLogin_ReturnsJwtResponseDTO() throws Exception {
        UserDetails userDetails = new User("john_doe", "newPassword123", Collections.emptyList());
        when(userDetailsServiceS.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken(userDetails, "newPassword123", userDetails.getAuthorities()));
        when(jwtService.generateToken(anyString())).thenReturn("testAccessToken");

        Users user = new Users();
        user.setId(1L);
        when(usersService.getUserByUsername(anyString())).thenReturn(new UserDTO());

        try {
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"usernameOrEmail\":\"john_doe\",\"password\":\"newPassword123\"}"))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    @WithMockUser(username = "john_doe", password = "newPassword123", roles = {"USER"})
    public void refreshToken_ValidToken_ReturnsJwtResponseDTO() throws Exception {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("testRefreshToken");
        refreshToken.setUser(new Users());
        when(refreshTokenService.findByToken(anyString())).thenReturn(Optional.of(refreshToken));
        when(refreshTokenService.verifyExpiration(refreshToken)).thenReturn(refreshToken);
        when(jwtService.generateToken(anyString())).thenReturn("testAccessToken");

        mockMvc.perform(post("/auth/refreshToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":\"testRefreshToken\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "john_doe", password = "newPassword123", roles = {"USER"})
    public void logout_ByUserId_Success() throws Exception {
        mockMvc.perform(post("/auth/logout/1"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "john_doe", password = "newPassword123", roles = {"USER"})
    public void logout_NoUserId_Success() throws Exception {
        mockMvc.perform(post("/auth/logout"))
                .andExpect(status().isOk());
    }
}
