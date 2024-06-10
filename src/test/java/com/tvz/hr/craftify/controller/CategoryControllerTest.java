package com.tvz.hr.craftify.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvz.hr.craftify.config.SecurityConfiguration;
import com.tvz.hr.craftify.repository.CategoryRepository;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.*;
import com.tvz.hr.craftify.service.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@Import(SecurityConfiguration.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private UsersService userService;

    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"})
    public void getCategories_ReturnsCategoryList() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(Collections.singletonList(new CategoryDTO()));

        mockMvc.perform(get("/category/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"})
    public void getCategory_ExistingId_ReturnsCategory() throws Exception {
        CategoryGetDTO category = new CategoryGetDTO();
        category.setId(1L);
        when(categoryService.getCategory(1L)).thenReturn(java.util.Optional.of(category));

        mockMvc.perform(get("/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"})
    public void getCategory_NonExistingId_ReturnsNotFound() throws Exception {
        when(categoryService.getCategory(anyLong())).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/category/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void createCategory_ValidCategory_ReturnsCreated() throws Exception {
        CategoryPostPutDTO category = new CategoryPostPutDTO();
        category.setName("Test Category");

        CategoryGetDTO createdCategory = new CategoryGetDTO();
        createdCategory.setId(1L);
        createdCategory.setName("Test Category");

        when(categoryService.createCategory(any(CategoryPostPutDTO.class))).thenReturn(createdCategory);

        mockMvc.perform(post("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Category"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void updateCategory_ExistingIdAndValidCategory_ReturnsOk() throws Exception {
        CategoryPostPutDTO category = new CategoryPostPutDTO();
        category.setName("Updated Category");

        CategoryGetDTO updatedCategory = new CategoryGetDTO();
        updatedCategory.setId(1L);
        updatedCategory.setName("Updated Category");

        when(categoryService.updateCategory(any(CategoryPostPutDTO.class), anyLong())).thenReturn(updatedCategory);

        mockMvc.perform(put("/category/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Category"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void updateCategory_NonExistingId_ReturnsNotFound() throws Exception {
        CategoryPostPutDTO category = new CategoryPostPutDTO();
        category.setName("Updated Category");

        when(categoryService.updateCategory(any(CategoryPostPutDTO.class), anyLong())).thenThrow(new RuntimeException());

        mockMvc.perform(put("/category/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void deleteCategory_ExistingId_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/category/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void deleteCategory_NonExistingId_ReturnsNotFound() throws Exception {
        mockMvc.perform(delete("/category/100"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void addUserPreference_ValidInput_ReturnsOk() throws Exception {
        List<UserPreferencesDTO> userPreferencesDTOList = new ArrayList<>();
        UserPreferencesDTO userPreferencesDTO = new UserPreferencesDTO();
        userPreferencesDTO.setUserId(1L);
        userPreferencesDTO.setCategoryId(1L);
        userPreferencesDTOList.add(userPreferencesDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/category/preference")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userPreferencesDTOList)))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).addUserPreference(anyLong(), anyLong());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void removeUserPreference_ValidInput_ReturnsOk() throws Exception {
        UserPreferencesDTO userPreferencesDTO = new UserPreferencesDTO();
        userPreferencesDTO.setUserId(1L);
        userPreferencesDTO.setCategoryId(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/category/preference")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userPreferencesDTO)))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).removeUserPreference(anyLong(), anyLong());
    }


    @Test
    @WithMockUser(roles = {"USER"})
    void getPreferredProjects_ValidUserId_ReturnsOk() throws Exception {
        List<CategoryDTO> categoryDTOList = new ArrayList<>();

        when(categoryService.getUserPreferences(anyLong())).thenReturn(Optional.of(categoryDTOList));

        List<ProjectGetDTO> projectGetDTOList = new ArrayList<>();

        when(projectService.getProjectsByCategory(anyLong())).thenReturn(Optional.of(projectGetDTOList));

        mockMvc.perform(MockMvcRequestBuilders.get("/category/preference/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(categoryService, times(1)).getUserPreferences(anyLong());
        verify(projectService, times(categoryDTOList.size())).getProjectsByCategory(anyLong());
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void getPreferredProjects_InvalidUserId_ReturnsInternalServerError() throws Exception {
        when(categoryService.getUserPreferences(any(Long.class))).thenThrow(new RuntimeException());

        mockMvc.perform(get("/category/preference/1"))
                .andExpect(status().isInternalServerError());
    }
}
