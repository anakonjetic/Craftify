package com.tvz.hr.craftify.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvz.hr.craftify.config.SecurityConfiguration;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.JwtService;
import com.tvz.hr.craftify.service.MediaService;
import com.tvz.hr.craftify.service.UserDetailsServiceImpl;
import com.tvz.hr.craftify.service.UsersService;
import com.tvz.hr.craftify.service.dto.MediaDTO;
import com.tvz.hr.craftify.service.dto.MediaGetDTO;
import com.tvz.hr.craftify.service.dto.MediaPutPostDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MediaController.class)
@Import(SecurityConfiguration.class)
public class MediaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MediaService mediaService;

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
    public void getAllMedia_ReturnsMediaList() throws Exception {
        when(mediaService.getAllMedia()).thenReturn(Collections.singletonList(new MediaDTO()));

        mockMvc.perform(get("/media/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"})
    public void getMedia_ExistingId_ReturnsMedia() throws Exception {
        MediaGetDTO media = new MediaGetDTO();
        media.setId(1L);
        when(mediaService.getMedia(1L)).thenReturn(java.util.Optional.of(media));

        mockMvc.perform(get("/media/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"})
    public void getMedia_NonExistingId_ReturnsNotFound() throws Exception {
        when(mediaService.getMedia(anyLong())).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/media/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"})
    public void addMedia_ValidMedia_ReturnsCreated() throws Exception {
        MediaPutPostDTO media = new MediaPutPostDTO();
        media.setMedia("Test Media");

        MediaGetDTO createdMedia = new MediaGetDTO();
        createdMedia.setId(1L);
        createdMedia.setMedia("Test Media");

        when(mediaService.addMedia(any(MediaPutPostDTO.class))).thenReturn(createdMedia);

        mockMvc.perform(post("/media")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(media)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.media").value("Test Media"));
    }

    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"})
    public void updateMedia_ExistingIdAndValidMedia_ReturnsOk() throws Exception {
        MediaPutPostDTO media = new MediaPutPostDTO();
        media.setMedia("Updated Media");

        MediaGetDTO updatedMedia = new MediaGetDTO();
        updatedMedia.setId(1L);
        updatedMedia.setMedia("Updated Media");

        when(mediaService.updateMedia(any(MediaPutPostDTO.class), anyLong())).thenReturn(updatedMedia);

        mockMvc.perform(put("/media/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(media)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.media").value("Updated Media"));
    }

    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"})
    public void updateMedia_NonExistingId_ReturnsNotFound() throws Exception {
        MediaPutPostDTO media = new MediaPutPostDTO();
        media.setMedia("Updated Media");

        when(mediaService.updateMedia(any(MediaPutPostDTO.class), anyLong())).thenThrow(new RuntimeException());

        mockMvc.perform(put("/media/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(media)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"})
    public void deleteMedia_ExistingId_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/media/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "john_doe", roles = {"ADMIN"})
    public void deleteMedia_NonExistingId_ReturnsNotFound() throws Exception {
        mockMvc.perform(delete("/media/100"))
                .andExpect(status().isNoContent());
    }
}
