package com.tvz.hr.craftify.controller;

import com.tvz.hr.craftify.config.SecurityConfiguration;
import com.tvz.hr.craftify.model.RefreshToken;
import com.tvz.hr.craftify.model.Users;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.JwtService;
import com.tvz.hr.craftify.service.RefreshTokenService;
import com.tvz.hr.craftify.service.UserDetailsServiceImpl;
import com.tvz.hr.craftify.service.UsersService;
import com.tvz.hr.craftify.service.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorizationController.class)
@Import(SecurityConfiguration.class)
public class AuthorizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private UsersService usersService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private RefreshTokenService refreshTokenService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*@Test
    @WithMockUser(username = "john_doe", password = "newPassword123", roles = {"USER"})
    public void authenticateAndGetToken_ValidLogin_ReturnsJwtResponseDTO() throws Exception {
        UserDetails userDetails = new User("john_doe", "newPassword123", Collections.emptyList());
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken(userDetails, "newPassword123", userDetails.getAuthorities()));
        when(jwtService.generateToken(anyString())).thenReturn("testAccessToken");

        Users user = new Users();
        user.setId(1L);
        user.setUsername("john_doe");
        UserDTO userDTO = new UserDTO();

        usersRepository.save(user);


        when(usersService.getUserByUsername(anyString())).thenReturn(userDTO);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("testRefreshToken");
        refreshToken.setUser(user);
        when(refreshTokenService.createRefreshToken(anyString())).thenReturn(refreshToken);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usernameOrEmail\":\"john_doe\",\"password\":\"newPassword123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("testAccessToken"))
                .andExpect(jsonPath("$.token").value("testRefreshToken"))
                .andExpect(jsonPath("$.user").exists());

        verify(userDetailsService, times(1)).loadUserByUsername(anyString());
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtService, times(1)).generateToken(anyString());
        verify(usersService, times(1)).getUserByUsername(anyString());
        verify(refreshTokenService, times(1)).createRefreshToken(anyString());
    }*/


    @Test
    public void authenticateAndGetToken_InvalidLogin_ThrowsException() throws Exception {
        when(authenticationManager.authenticate(any())).thenThrow(new UsernameNotFoundException("Invalid user request"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usernameOrEmail\":\"invalid_user\",\"password\":\"wrongPassword\"}"))
                .andExpect(status().isInternalServerError());

        verify(authenticationManager, times(1)).authenticate(any());
        verifyNoMoreInteractions(userDetailsService, jwtService, usersService, refreshTokenService);
    }

    @Test
    @WithMockUser(username = "john_doe", password = "newPassword123", roles = {"USER"})
    public void refreshToken_ValidToken_ReturnsJwtResponseDTO() throws Exception {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("testRefreshToken");
        Users user = new Users();
        user.setUsername("john_doe");
        refreshToken.setUser(user);
        when(refreshTokenService.findByToken(anyString())).thenReturn(Optional.of(refreshToken));
        when(refreshTokenService.verifyExpiration(refreshToken)).thenReturn(refreshToken);
        when(jwtService.generateToken(anyString())).thenReturn("testAccessToken");

        mockMvc.perform(post("/auth/refreshToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":\"testRefreshToken\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("testAccessToken"))
                .andExpect(jsonPath("$.token").value("testRefreshToken"));

        verify(refreshTokenService, times(1)).findByToken(anyString());
        verify(refreshTokenService, times(1)).verifyExpiration(refreshToken);
        verify(jwtService, times(1)).generateToken(anyString());
    }

    @Test
    @WithMockUser(username = "john_doe", password = "newPassword123", roles = {"USER"})
    public void refreshToken_InvalidToken_ThrowsException() throws Exception {
        when(refreshTokenService.findByToken(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/refreshToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":\"invalidToken\"}"))
                .andExpect(status().isInternalServerError());

        verify(refreshTokenService, times(1)).findByToken(anyString());
        verifyNoMoreInteractions(refreshTokenService, jwtService);
    }

    @Test
    @WithMockUser(username = "john_doe", password = "newPassword123", roles = {"USER"})
    public void logout_ByUserId_Success() throws Exception {
        doNothing().when(refreshTokenService).removeToken(anyLong());

        mockMvc.perform(post("/auth/logout/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Logged out successfully"));

        verify(refreshTokenService, times(1)).removeToken(anyLong());
    }

    @Test
    @WithMockUser(username = "john_doe", password = "newPassword123", roles = {"USER"})
    public void logout_NoUserId_Success() throws Exception {
        doNothing().when(refreshTokenService).removeToken(any(HttpServletRequest.class));

        mockMvc.perform(post("/auth/logout"))
                .andExpect(status().isOk());

        verify(refreshTokenService, times(1)).removeToken(any(HttpServletRequest.class));
    }
}
