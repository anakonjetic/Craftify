package com.tvz.hr.craftify.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvz.hr.craftify.config.SecurityConfiguration;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.JwtService;
import com.tvz.hr.craftify.service.SubscriptionService;
import com.tvz.hr.craftify.service.UserDetailsServiceImpl;
import com.tvz.hr.craftify.service.UsersService;
import com.tvz.hr.craftify.service.dto.ProjectDTO;
import com.tvz.hr.craftify.service.dto.SubscriptionDTO;
import com.tvz.hr.craftify.service.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubscriptionController.class)
@Import(SecurityConfiguration.class)
public class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubscriptionService subscriptionService;


    @MockBean
    private UsersService userService;

    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"})
    public void testGetFollowers() throws Exception {
        when(subscriptionService.getUserFollowers(anyLong())).thenReturn(Optional.of(Collections.singletonList(new UserDTO())));

        mockMvc.perform(get("/subscription/followers/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"})
    public void testGetFollowedUsers() throws Exception {
        when(subscriptionService.getUserFollowings(anyLong())).thenReturn(Optional.of(Collections.singletonList(new UserDTO())));

        mockMvc.perform(get("/subscription/followed/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"})
    public void testGetFollowedProjects() throws Exception {
        when(subscriptionService.getUserProjectFollowings(anyLong())).thenReturn(Optional.of(Collections.singletonList(new ProjectDTO())));

        mockMvc.perform(get("/subscription/followed/projects/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"})
    public void testGetProjectFollowers() throws Exception {
        when(subscriptionService.getProjectFollowers(anyLong())).thenReturn(Optional.of(Collections.singletonList(new UserDTO())));

        mockMvc.perform(get("/subscription/followers/project/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"})
    public void testFollowUser() throws Exception {
        mockMvc.perform(post("/subscription/user")
                        .content(asJsonString(new SubscriptionDTO()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"})
    public void testUnfollowUser() throws Exception {
        mockMvc.perform(delete("/subscription/user")
                        .content(asJsonString(new SubscriptionDTO()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"})
    public void testFollowProject() throws Exception {
        mockMvc.perform(post("/subscription/project")
                        .content(asJsonString(new SubscriptionDTO()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    public void testUnfollowProject() throws Exception {
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        subscriptionDTO.setUserId(1L);
        subscriptionDTO.setFollowingId(2L);

        mockMvc.perform(delete("/subscription/project")
                        .content(asJsonString(subscriptionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
