package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.*;
import com.tvz.hr.craftify.repository.MediaRepository;
import com.tvz.hr.craftify.repository.TutorialRepository;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.*;
import com.tvz.hr.craftify.service.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Sql("/data.sql")
public class TutorialServiceImplTest {

    @Mock
    private TutorialRepository tutorialRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private UsersService usersService;

    @Mock
    private ComplexityService complexityService;

    @Mock
    private MediaRepository mediaRepository;

    @Mock
    private UserAuthorizationService userAuthorizationService;

    @Mock
    private LoggedUserContentService loggedUserContentService;

    @Mock
    private GuestUserContentService guestUserContentService;

    @InjectMocks
    private TutorialServiceImpl tutorialService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*@Test
    public void testGetAllTutorials() {
        // Given
        List<Tutorial> tutorials = new ArrayList<>();
        tutorials.add(new Tutorial());

        when(tutorialRepository.findAll()).thenReturn(tutorials);

        List<TutorialDTO> tutorialDTOs = tutorialService.getAllTutorials();

        // Then
        assertEquals(1, tutorialDTOs.size());
    }*/
    @Test
    public void testGetAllTutorials_loggedInUser() {
        Users loggedInUser = new Users();
        loggedInUser.setId(1L);
        ContentService mockContentService = mock(ContentService.class);
        List<TutorialDTO> expectedTutorials = new ArrayList<>();

        when(mockContentService.getAllTutorials()).thenReturn(expectedTutorials);
        when(userAuthorizationService.getLoggedInUser()).thenReturn(loggedInUser);

        List<TutorialDTO> actualTutorials = tutorialService.getAllTutorials();

        assertEquals(expectedTutorials.size(), actualTutorials.size());
        verify(loggedUserContentService, times(1)).getAllTutorials();
        verify(guestUserContentService, never()).getAllProjects();
    }

    @Test
    public void testGetAllTutorials_guestUser() {
        ContentService mockContentService = mock(ContentService.class);
        List<TutorialDTO> expectedTutorials = new ArrayList<>();

        when(mockContentService.getAllTutorials()).thenReturn(expectedTutorials);
        when(userAuthorizationService.getLoggedInUser()).thenReturn(null);

        List<TutorialDTO> actualTutorials = tutorialService.getAllTutorials();

        assertEquals(expectedTutorials.size(), actualTutorials.size());
        verify(loggedUserContentService, never()).getAllProjects();
        verify(guestUserContentService, times(1)).getAllTutorials();
    }

    @Test
    public void testGetTutorialById() {
        Tutorial tutorial = new Tutorial();
        tutorial.setId(1L);
        when(tutorialRepository.findById(1L)).thenReturn(Optional.of(tutorial));

        Optional<TutorialDTO> tutorialDTO = tutorialService.getTutorialById(1L);

        assertTrue(tutorialDTO.isPresent());
        assertEquals(tutorial.getId(), tutorialDTO.get().getId());
    }


    @Test
    public void testDeleteTutorialById() {
        Long id = 1L;

        tutorialService.deleteTutorialById(id);

        verify(tutorialRepository, times(1)).deleteById(id);
    }

    @Test
    public void testCreateTutorial() {
        TutorialPostDTO postTutorial = new TutorialPostDTO();
        postTutorial.setTitle("Test Tutorial");
        postTutorial.setContent("Test content");
        postTutorial.setUserId(1L);
        postTutorial.setCategoryId(1L);
        postTutorial.setComplexityId(1L);
        List<Long> mediaList = new ArrayList<>();
        mediaList.add(1L);
        postTutorial.setMediaList(mediaList);

        UsersGetDTO userDTO = new UsersGetDTO();
        when(usersService.getUser(1L)).thenReturn(Optional.of(userDTO));

        Category category = new Category();
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(category));

        ComplexityGetDTO complexityDTO = new ComplexityGetDTO();
        when(complexityService.getComplexityById(1L)).thenReturn(Optional.of(complexityDTO));

        Media media = new Media();
        when(mediaRepository.findById(1L)).thenReturn(Optional.of(media));

        Tutorial savedTutorial = new Tutorial();
        savedTutorial.setId(1L);
        savedTutorial.setTitle(postTutorial.getTitle());
        savedTutorial.setContent(postTutorial.getContent());
        when(tutorialRepository.save(any(Tutorial.class))).thenReturn(savedTutorial);

        TutorialDTO createdTutorial = tutorialService.createTutorial(postTutorial);

        assertNotNull(createdTutorial, "Created tutorial should not be null");
        assertEquals(postTutorial.getTitle(), createdTutorial.getTitle(), "Title should match the input DTO title");
        assertEquals(postTutorial.getContent(), createdTutorial.getContent(), "Content should match the input DTO content");
    }




    @Test
    public void testUpdateTutorial() {
        TutorialPutDTO putTutorial = new TutorialPutDTO();
        putTutorial.setTitle("Updated Tutorial");
        putTutorial.setContent("Updated content");
        putTutorial.setCategoryId(1L);
        putTutorial.setComplexityId(1L);

        Tutorial existingTutorial = new Tutorial();
        when(tutorialRepository.findById(1L)).thenReturn(Optional.of(existingTutorial));

        Category category = new Category(); // Assuming category data
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(category));

        ComplexityGetDTO complexityDTO = new ComplexityGetDTO();
        when(complexityService.getComplexityById(1L)).thenReturn(Optional.of(complexityDTO));

        Tutorial updatedTutorialEntity = new Tutorial();
        updatedTutorialEntity.setId(1L);
        updatedTutorialEntity.setTitle("Updated Tutorial");
        updatedTutorialEntity.setContent("Updated content");
        when(tutorialRepository.save(any(Tutorial.class))).thenReturn(updatedTutorialEntity);

        TutorialDTO updatedTutorial = tutorialService.updateTutorial(putTutorial, 1L);

        assertNotNull(updatedTutorial);
        assertEquals(putTutorial.getTitle(), updatedTutorial.getTitle());
        assertEquals(putTutorial.getContent(), updatedTutorial.getContent());
    }


}