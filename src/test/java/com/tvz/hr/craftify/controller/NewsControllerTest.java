package com.tvz.hr.craftify.controller;

import com.tvz.hr.craftify.config.SecurityConfiguration;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.JwtService;
import com.tvz.hr.craftify.service.NewsService;
import com.tvz.hr.craftify.service.UserDetailsServiceImpl;
import com.tvz.hr.craftify.service.UsersService;
import com.tvz.hr.craftify.service.dto.FilterNewsDTO;
import com.tvz.hr.craftify.service.dto.NewsDTO;
import com.tvz.hr.craftify.service.dto.NewsPostPutDTO;
import org.junit.jupiter.api.Test;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NewsController.class)
@Import(SecurityConfiguration.class)
public class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsService newsService;

    @MockBean
    private UsersService userService;

    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void getAllNews_ReturnsNewsList() throws Exception {
        NewsDTO newsDTO = new NewsDTO();
        when(newsService.getAllNews()).thenReturn(Collections.singletonList(newsDTO));

        mockMvc.perform(get("/news/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    public void getNewsById_ExistingId_ReturnsNews() throws Exception {
        NewsDTO newsDTO = new NewsDTO();
        when(newsService.getNewsById(anyLong())).thenReturn(Optional.of(newsDTO));

        mockMvc.perform(get("/news/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    public void getNewsById_NonExistingId_ReturnsNotFound() throws Exception {
        when(newsService.getNewsById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/news/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createNews_AsAdmin_ReturnsCreatedNews() throws Exception {
        NewsDTO newsDTO = new NewsDTO();
        NewsPostPutDTO newsPostPutDTO = new NewsPostPutDTO();
        when(newsService.createNews(any(NewsDTO.class))).thenReturn(newsPostPutDTO);

        mockMvc.perform(post("/news/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Sample Title\", \"content\":\"Sample Content\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void createNews_AsUser_ReturnsForbidden() throws Exception {
        mockMvc.perform(post("/news/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Sample Title\", \"content\":\"Sample Content\"}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateNews_AsAdmin_ReturnsUpdatedNews() throws Exception {
        NewsDTO newsDTO = new NewsDTO();
        NewsPostPutDTO newsPostPutDTO = new NewsPostPutDTO();
        when(newsService.updateNews(anyLong(), any(NewsDTO.class))).thenReturn(newsPostPutDTO);

        mockMvc.perform(put("/news/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Title\", \"content\":\"Updated Content\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updateNews_AsUser_ReturnsForbidden() throws Exception {
        mockMvc.perform(put("/news/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Title\", \"content\":\"Updated Content\"}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deleteNews_AsAdmin_ReturnsNoContent() throws Exception {
        doNothing().when(newsService).deleteNews(anyLong());

        mockMvc.perform(delete("/news/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(newsService, times(1)).deleteNews(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteNews_AsUser_ReturnsForbidden() throws Exception {
        mockMvc.perform(delete("/news/{id}", 1L))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void getFilteredNews_ValidFilter_ReturnsFilteredNews() throws Exception {
        FilterNewsDTO filterNewsDTO = new FilterNewsDTO();
        filterNewsDTO.setCategoryId(1L);
        NewsDTO newsDTO = new NewsDTO();
        when(newsService.getFilteredNews(any(FilterNewsDTO.class))).thenReturn(Collections.singletonList(newsDTO));

        mockMvc.perform(post("/news/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    public void getFilteredNews_NoCategoryId_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/news/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getFilteredNews_NoResults_ReturnsNoContent() throws Exception {
        FilterNewsDTO filterNewsDTO = new FilterNewsDTO();
        filterNewsDTO.setCategoryId(1L);
        when(newsService.getFilteredNews(any(FilterNewsDTO.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/news/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryId\":1}"))
                .andExpect(status().isNoContent());
    }
}
