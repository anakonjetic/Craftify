package com.tvz.hr.craftify.controller;

import com.tvz.hr.craftify.config.SecurityConfiguration;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.ComplexityService;
import com.tvz.hr.craftify.service.JwtService;
import com.tvz.hr.craftify.service.UserDetailsServiceImpl;
import com.tvz.hr.craftify.service.UsersService;
import com.tvz.hr.craftify.service.dto.ComplexityGetDTO;
import com.tvz.hr.craftify.service.dto.ComplexityPostPutDTO;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ComplexityController.class)
@Import(SecurityConfiguration.class)
public class ComplexityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComplexityService complexityService;

    @MockBean
    private UsersService userService;

    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllComplexities() throws Exception {
        when(complexityService.getAllComplexities()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/complexity/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(complexityService, times(1)).getAllComplexities();
    }

    @Test
    public void testGetComplexity() throws Exception {
        ComplexityGetDTO complexityGetDTO = new ComplexityGetDTO();
        when(complexityService.getComplexityById(anyLong())).thenReturn(Optional.of(complexityGetDTO));

        mockMvc.perform(get("/complexity/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(complexityService, times(1)).getComplexityById(anyLong());
    }

    @Test
    public void testGetComplexity_NotFound() throws Exception {
        when(complexityService.getComplexityById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/complexity/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(complexityService, times(1)).getComplexityById(anyLong());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateComplexity() throws Exception {
        ComplexityGetDTO complexityGetDTO = new ComplexityGetDTO();
        when(complexityService.createComplexity(any())).thenReturn(complexityGetDTO);

        mockMvc.perform(post("/complexity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Complexity\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists());

        verify(complexityService, times(1)).createComplexity(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateComplexity() throws Exception {
        ComplexityGetDTO complexityGetDTO = new ComplexityGetDTO();
        when(complexityService.updateComplexity(any(), anyLong())).thenReturn(complexityGetDTO);

        mockMvc.perform(put("/complexity/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Complexity\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(complexityService, times(1)).updateComplexity(any(), anyLong());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateComplexity_NotFound() throws Exception {
        when(complexityService.updateComplexity(any(), anyLong())).thenThrow(new RuntimeException("Complexity not found"));

        mockMvc.perform(put("/complexity/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Complexity\"}"))
                .andExpect(status().isNotFound());

        verify(complexityService, times(1)).updateComplexity(any(), anyLong());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteComplexity() throws Exception {
        mockMvc.perform(delete("/complexity/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(complexityService, times(1)).deleteComplexity(anyLong());
    }
}
