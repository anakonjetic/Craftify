package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.*;
import com.tvz.hr.craftify.repository.CategoryRepository;
import com.tvz.hr.craftify.repository.ProjectRepository;
import com.tvz.hr.craftify.repository.TutorialRepository;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.CategoryServiceImpl;
import com.tvz.hr.craftify.service.dto.*;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
import com.tvz.hr.craftify.utilities.exceptions.ApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.User;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TutorialRepository tutorialRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCategories_ReturnsListOfCategoryDTOs() {
        // Create some sample categories
        Category category1 = new Category("Category 1", new ArrayList<>(), new ArrayList<>());
        Category category2 = new Category("Category 2", new ArrayList<>(), new ArrayList<>());
        List<Category> categories = Arrays.asList(category1, category2);

        when(categoryRepository.findAll()).thenReturn(categories);

        List<CategoryDTO> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getCategory_ReturnsCategoryDTO_WhenCategoryExists() {
        // Create a sample category
        Category category = new Category("Category 1", new ArrayList<>(), new ArrayList<>());
        category.setId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Optional<CategoryGetDTO> result = categoryService.getCategory(1L);

        assertTrue(result.isPresent());
        assertEquals("Category 1", result.get().getName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void getCategory_ReturnsEmptyOptional_WhenCategoryDoesNotExist() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<CategoryGetDTO> result = categoryService.getCategory(1L);

        assertFalse(result.isPresent());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void createCategory_CreatesNewCategory_WhenValidCategoryPostPutDTOIsGiven() {
        CategoryPostPutDTO categoryPostPutDTO = new CategoryPostPutDTO();
        categoryPostPutDTO.setName("New Category");
        categoryPostPutDTO.setProjectIdList(new ArrayList<Long>(1));
        categoryPostPutDTO.setTutorialIdList(new ArrayList<Long>(1));

        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category savedCategory = invocation.getArgument(0);
            savedCategory.setId(1L);
            return savedCategory;
        });

        CategoryGetDTO result = categoryService.createCategory(categoryPostPutDTO);

        assertNotNull(result);
        assertEquals("New Category", result.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void updateCategory_UpdatesCategory_WhenValidCategoryPostPutDTOIsGiven() {
        // Create a sample category post put DTO
        CategoryPostPutDTO categoryPostPutDTO = new CategoryPostPutDTO();
        categoryPostPutDTO.setName("Updated Category");
        categoryPostPutDTO.setProjectIdList(Collections.singletonList(1L));
        categoryPostPutDTO.setTutorialIdList(Collections.singletonList(2L));

        // Create a sample category
        Category category = new Category("Category", new ArrayList<>(), new ArrayList<>());
        category.setId(1L);

        // Mock the repository's behavior
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the service method
        CategoryGetDTO result = categoryService.updateCategory(categoryPostPutDTO, 1L);

        // Assert the result
        assertNotNull(result);
        assertEquals("Updated Category", result.getName());
        assertEquals(0, result.getProjects().size());
        assertEquals(0, result.getTutorials().size());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testDeleteCategory() {
        long categoryIdToDelete = 1L;

        categoryService.deleteCategory(categoryIdToDelete);

        verify(categoryRepository, times(1)).deleteById(categoryIdToDelete);
    }

    @Test
    void testAddUserPreference() {
        long categoryId = 1L;
        long userId = 1L;
        Category category = new Category();
        Users user = new Users();
        user.setId(userId);
        category.setUserPreferences(new ArrayList<Users>(){
            Users user1 = user;
        });

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDTO result = categoryService.addUserPreference(categoryId, userId);

        assertNotNull(result);
    }

    @Test
    void testRemoveUserPreference() {
        // Setup
        long categoryId = 1L;
        long userId = 1L;
        Category category = new Category();
        Users user = new Users();
        user.setId(userId);
        category.setUserPreferences(new ArrayList<Users>(){
            Users user1 = user;
        });

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDTO result = categoryService.removeUserPreference(categoryId, userId);

        assertNotNull(result);
    }

    @Test
    void testGetUserPreferences() {
        // Setup
        long userId = 1L;
        List<Long> categoryIds = new ArrayList<>();
        categoryIds.add(1L);
        categoryIds.add(2L);

        when(categoryRepository.findPreferredCategoryIdsByUserId(userId)).thenReturn(Optional.of(categoryIds));
        when(categoryRepository.findAllById(categoryIds)).thenReturn(new ArrayList<>());

        Optional<List<CategoryDTO>> result = categoryService.getUserPreferences(userId);

        assertTrue(result.isPresent());
        assertTrue(result.get().isEmpty());
    }
}
