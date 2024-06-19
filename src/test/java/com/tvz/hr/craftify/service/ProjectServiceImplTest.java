package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.*;
import com.tvz.hr.craftify.repository.ProjectRepository;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.dto.*;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
import com.tvz.hr.craftify.utilities.exceptions.ApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.TransientDataAccessException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private UsersService usersService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private ComplexityService complexityService;

    @Mock
    private MediaService mediaService;
    @Mock
    private UserAuthorizationService userAuthorizationService;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllProjects() {
        List<Project> projects = List.of(new Project(), new Project());
        projects.get(0).setMediaList(new ArrayList<>());
        projects.get(0).setComments(new ArrayList<>());
        projects.get(0).setUserLikes(new ArrayList<>());
        projects.get(0).setFavoriteProjects(new ArrayList<>());
        projects.get(0).setProjectFollowers(new ArrayList<>());
        projects.get(1).setMediaList(new ArrayList<>());
        projects.get(1).setComments(new ArrayList<>());
        projects.get(1).setUserLikes(new ArrayList<>());
        projects.get(1).setFavoriteProjects(new ArrayList<>());
        projects.get(1).setProjectFollowers(new ArrayList<>());
        when(projectRepository.findAll()).thenReturn(projects);

        List<ProjectDTO> result = projectService.getAllProjects();

        assertEquals(2, result.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void getProjectById() {
        Project project = new Project();
        project.setMediaList(new ArrayList<>());
        project.setComments(new ArrayList<>());
        project.setUserLikes(new ArrayList<>());
        project.setFavoriteProjects(new ArrayList<>());
        project.setProjectFollowers(new ArrayList<>());
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        Optional<ProjectDTO> result = projectService.getProjectById(1L);

        assertTrue(result.isPresent());
        verify(projectRepository, times(1)).findById(anyLong());
    }

    @Test
    void getUsersWhoLikedProject() {
        Project project = new Project();
        project.setUserLikes(List.of(new Users(), new Users()));
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        Optional<List<UserDTO>> result = projectService.getUsersWhoLikedProject(1L);

        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
        verify(projectRepository, times(1)).findById(anyLong());
    }

    @Test
    void createProject() {
        UsersGetDTO userDTO = new UsersGetDTO();
        userDTO.setId(1L);
        when(usersService.getUser(anyLong())).thenReturn(Optional.of(userDTO));
        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(new Users()));
        when(categoryService.getCategoryById(anyLong())).thenReturn(Optional.of(new Category()));
        when(complexityService.getComplexityById(anyLong())).thenReturn(Optional.of(new ComplexityGetDTO()));
        when(projectRepository.save(any(Project.class))).thenReturn(new Project());

        ProjectPostDTO postDTO = new ProjectPostDTO();
        postDTO.setUserId(1L);
        postDTO.setCategoryId(1L);
        postDTO.setComplexityId(1L);
        postDTO.setMediaList(new ArrayList<>());

        Project result = projectService.createProject(postDTO);

        assertNotNull(result);
        verify(usersService, times(1)).getUser(anyLong());
        verify(categoryService, times(1)).getCategoryById(anyLong());
        verify(complexityService, times(1)).getComplexityById(anyLong());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void updateProject() {
        Project project = new Project();
        project.setUser(new Users());
        project.setMediaList(new ArrayList<>());

        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
        when(categoryService.getCategoryById(anyLong())).thenReturn(Optional.of(new Category()));
        when(complexityService.getComplexityById(anyLong())).thenReturn(Optional.of(new ComplexityGetDTO()));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectPutDTO putDTO = new ProjectPutDTO();
        putDTO.setCategoryId(1L);
        putDTO.setComplexityId(1L);
        putDTO.setMediaList(new ArrayList<>());

        Project result = projectService.updateProject(putDTO, 1L);

        assertNotNull(result);
        verify(projectRepository, times(1)).findById(anyLong());
        verify(categoryService, times(1)).getCategoryById(anyLong());
        verify(complexityService, times(1)).getComplexityById(anyLong());
        verify(projectRepository, times(1)).save(any(Project.class));
    }


    @Test
    void deleteProject() {
        doNothing().when(projectRepository).deleteFavoritesByProjectId(anyLong());
        doNothing().when(projectRepository).deleteProjectSubscribersByProjectId(anyLong());
        doNothing().when(projectRepository).deleteUserProjectLikesByProjectId(anyLong());
        doNothing().when(projectRepository).deleteById(anyLong());

        projectService.deleteProject(1L);

        verify(projectRepository, times(1)).deleteFavoritesByProjectId(anyLong());
        verify(projectRepository, times(1)).deleteProjectSubscribersByProjectId(anyLong());
        verify(projectRepository, times(1)).deleteUserProjectLikesByProjectId(anyLong());
        verify(projectRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void getFilteredProjects() {
        List<Project> projects = List.of(
                new Project(1L, "DIY Mason Jar Lanterns", "", "",  new Users(), new Category(), new Complexity()),
                new Project(2L, "DIY Fabric Wall Art", "", "", new Users(), new Category(), new Complexity())
        );

        when(projectRepository.findByFilters(anyString(), anyLong(), anyLong())).thenReturn(projects);

        FilterProjectDTO filterDTO = new FilterProjectDTO();
        filterDTO.setNameOrUser("DIY");
        filterDTO.setCategoryId(1L);
        filterDTO.setComplexityId(1L);

        Optional<List<ProjectGetDTO>> result = projectService.getFilteredProjects(filterDTO);

        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
        verify(projectRepository, times(1)).findByFilters(anyString(), anyLong(), anyLong());
    }


    @Test
    void getProjectsByCategory() {
        List<Project> projects = List.of(new Project(), new Project());
        when(projectRepository.findByCategory_Id(anyLong())).thenReturn(projects);

        Optional<List<ProjectGetDTO>> result = projectService.getProjectsByCategory(1L);

        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
        verify(projectRepository, times(1)).findByCategory_Id(anyLong());
    }

    @Test
    void getProjectsByUserPreference() {
        // Mock user preferences
        List<CategoryDTO> userPreferences = List.of(
                new CategoryDTO(2L, "Category 2"),
                new CategoryDTO(3L, "Category 3"),
                new CategoryDTO(4L, "Category 4")
        );

        UsersGetDTO userDTO = new UsersGetDTO();
        userDTO.setId(1L);
        userDTO.setUserPreferences(userPreferences);

        List<Project> projectsCategory2 = List.of(new Project(1L, "Project 1", "Description 1", "Content 1", new Users(), new Category(), new Complexity()));
        List<Project> projectsCategory3 = List.of(new Project(2L, "Project 2", "Description 2", "Content 2",  new Users(), new Category(), new Complexity()));
        List<Project> projectsCategory4 = List.of(new Project(3L, "Project 3", "Description 3", "Content 3", new Users(), new Category(), new Complexity()));

        projectsCategory2.get(0).setUserLikes(new ArrayList<>());
        projectsCategory3.get(0).setUserLikes(new ArrayList<>());
        projectsCategory4.get(0).setUserLikes(new ArrayList<>());

        when(usersService.getUser(anyLong())).thenReturn(Optional.of(userDTO));
        when(projectRepository.findByCategory_Id(2L)).thenReturn(projectsCategory2);
        when(projectRepository.findByCategory_Id(3L)).thenReturn(projectsCategory3);
        when(projectRepository.findByCategory_Id(4L)).thenReturn(projectsCategory4);

        Optional<List<ProjectGetDTO>> result = projectService.getProjectsByUserPreference(1L);

        assertTrue(result.isPresent());
        assertEquals(3, result.get().size());

        verify(usersService, times(1)).getUser(anyLong());
        verify(projectRepository, times(1)).findByCategory_Id(2L);
        verify(projectRepository, times(1)).findByCategory_Id(3L);
        verify(projectRepository, times(1)).findByCategory_Id(4L);
    }

    @Test
    void getProjectsByUserPreference_UserNotFound_ThrowsRuntimeException() {
        when(usersService.getUser(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            projectService.getProjectsByUserPreference(1L);
        });
    }

    @Test
    void createProject_UserNotFound_ThrowsRuntimeException() {
        when(usersService.getUser(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            projectService.createProject(new ProjectPostDTO());
        });
    }

    @Test
    void updateProject_ProjectNotFound_ThrowsRuntimeException() {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            projectService.updateProject(new ProjectPutDTO(), 1L);
        });
    }

    @Test
    void updateProject_InvalidComplexity_ThrowsRuntimeException() {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(new Project()));
        when(categoryService.getCategoryById(anyLong())).thenReturn(Optional.of(new Category()));
        when(complexityService.getComplexityById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            projectService.updateProject(new ProjectPutDTO(), 1L);
        });
    }


    @Test
    void getProjectsByCategory_DatabaseError_ThrowsApplicationException() {
        when(projectRepository.findByCategory_Id(anyLong())).thenThrow(new RuntimeException());

        assertThrows(ApplicationException.class, () -> {
            projectService.getProjectsByCategory(1L);
        });
    }

    @Test
    void getProjectsByUserPreference_DatabaseError_ThrowsApplicationException() {
        when(usersService.getUser(anyLong())).thenReturn(Optional.of(new UsersGetDTO()));
        when(projectRepository.findByCategory_Id(anyLong())).thenReturn(Collections.emptyList());

        assertThrows(ApplicationException.class, () -> {
            projectService.getProjectsByUserPreference(1L);
        });
    }}
