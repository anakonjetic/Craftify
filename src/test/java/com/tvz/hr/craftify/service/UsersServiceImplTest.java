package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.*;
import com.tvz.hr.craftify.repository.CategoryRepository;
import com.tvz.hr.craftify.repository.CommentRepository;
import com.tvz.hr.craftify.repository.ProjectRepository;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.tvz.hr.craftify.service.PasswordService.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UsersServiceImplTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private UsersServiceImpl usersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UserDetails userDetails = new User("john_doe", "password", List.of());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void getAllUsers() {
        Users user1 = new Users();
        user1.setId(1L);
        user1.setUsername("user1");

        Users user2 = new Users();
        user2.setId(2L);
        user2.setUsername("user2");

        when(usersRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<UsersGetDTO> users = usersService.getAllUsers();

        assertEquals(2, users.size());
    }

    @Test
    void getUser() {
        Users user = new Users();
        user.setId(1L);
        user.setUsername("user1");

        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<UsersGetDTO> result = usersService.getUser(1L);

        assertTrue(result.isPresent());
        assertEquals("user1", result.get().getUsername());
    }

    @Test
    void getLoggedInUser_AuthenticatedUser_ReturnsLoggedInUser() {
        Users user = new Users();
        user.setUsername("user1");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user1");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(usersRepository.findByUsername("user1")).thenReturn(user);

        Users loggedInUser = usersService.getLoggedInUser();

        assertNotNull(loggedInUser);
        assertEquals("user1", loggedInUser.getUsername());
    }

    @Test
    void getLoggedInUser_UnauthenticatedUser_ReturnsNull() {
        SecurityContextHolder.clearContext();
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Users result = usersService.getLoggedInUser();
        assertEquals(null, result);

        verify(usersRepository, never()).findByUsername(any());
    }

    @Test
    void getUserByUsername() {
        Users user = new Users();
        user.setUsername("user1");

        when(usersRepository.findByUsername("user1")).thenReturn(user);

        UserDTO userDTO = usersService.getUserByUsername("user1");

        assertNotNull(userDTO);
        assertEquals("user1", userDTO.getUsername());
    }

    @Test
    void checkAuthorization() {
        Users user = new Users();
        user.setId(1L);
        user.setAdmin(false);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user1");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(usersRepository.findByUsername("user1")).thenReturn(user);

        assertThrows(RuntimeException.class, () -> {
            usersService.checkAuthorization(2L);
        });
    }

    @Test
    void createUser_ValidUser_ReturnsUserDTO() {
        UsersPutPostDTO userDTO = new UsersPutPostDTO();
        userDTO.setName("user1");
        userDTO.setUsername("user1");
        userDTO.setEmail("user1@example.com");
        userDTO.setPassword("StrongPassword1!");
        userDTO.setAdmin(false);
        userDTO.setPrivate(false);
        userDTO.setUserPreferences(new ArrayList<>());

        when(usersRepository.save(any(Users.class))).thenAnswer(i -> i.getArguments()[0]);

        UsersGetDTO result = usersService.createUser(userDTO);

        assertNotNull(result);
        assertEquals("user1", result.getUsername());
    }

    @Test
    void createUser_InvalidPassword_ThrowsException() {
        UsersPutPostDTO userDTO = new UsersPutPostDTO();
        userDTO.setPassword("password");
        userDTO.setUserPreferences(new ArrayList<>());

        when(usersRepository.save(any(Users.class))).thenThrow(new IllegalArgumentException("Password is not strong enough"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usersService.createUser(userDTO);
        });

        assertEquals("Password is not strong enough", exception.getMessage());
    }

    @Test
    void updateUser_ValidUser_ReturnsUpdatedUser() {
        Users loggedInUser = new Users();
        loggedInUser.setId(1L);
        loggedInUser.setUsername("john_doe");

        when(usersRepository.findByUsername("john_doe")).thenReturn(loggedInUser);

        Users existingUser = new Users();
        existingUser.setId(1L);
        existingUser.setUsername("user1");

        UsersPutPostDTO userDTO = new UsersPutPostDTO();
        userDTO.setName("updatedName");
        userDTO.setUsername("updatedUsername");
        userDTO.setEmail("updated@example.com");
        userDTO.setPassword("UpdatedPassword1!");
        userDTO.setAdmin(false);
        userDTO.setPrivate(false);
        userDTO.setUserPreferences(new ArrayList<>());

        when(usersRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(usersRepository.save(any(Users.class))).thenAnswer(i -> i.getArguments()[0]);

        UsersGetDTO result = usersService.updateUser(userDTO, 1L);

        assertNotNull(result);
        assertEquals("updatedUsername", result.getUsername());
    }

    @Test
    void updateUser_NonExistingUser_ReturnsNull() {
        Users loggedInUser = new Users();
        loggedInUser.setId(1L);
        loggedInUser.setAdmin(true);
        loggedInUser.setUsername("john_doe");

        when(usersRepository.findByUsername("john_doe")).thenReturn(loggedInUser);

        UsersPutPostDTO userDTO = new UsersPutPostDTO();
        when(usersRepository.findById(999L)).thenReturn(Optional.empty());

        UsersGetDTO updatedUser = usersService.updateUser(userDTO, 999L);
        assertNull(updatedUser);

        verify(usersRepository, times(1)).findById(999L);
    }

    @Test
    void updateUser_WeakPassword_ThrowsException() {
        Users loggedInUser = new Users();
        loggedInUser.setId(1L);
        loggedInUser.setUsername("john_doe");

        when(usersRepository.findByUsername("john_doe")).thenReturn(loggedInUser);

        UsersPutPostDTO userDTO = new UsersPutPostDTO();
        userDTO.setName("Updated Name");
        userDTO.setPassword("weak");
        userDTO.setUserPreferences(new ArrayList<>());

        Users existingUser = new Users();
        existingUser.setId(1L);

        when(usersRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(categoryRepository.findById(any())).thenReturn(Optional.of(new Category()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usersService.updateUser(userDTO, 1L);
        });

        assertEquals("Password is not strong enough", exception.getMessage());
        verify(usersRepository, never()).save(existingUser);
    }

    @Test
    void changeUserPassword_ValidPassword_ReturnsUpdatedUser() {
        Users loggedInUser = new Users();
        loggedInUser.setId(1L);
        loggedInUser.setUsername("john_doe");

        when(usersRepository.findByUsername("john_doe")).thenReturn(loggedInUser);

        Users user = new Users();
        user.setId(1L);
        user.setPassword("OldPassword1!");

        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(usersRepository.save(any(Users.class))).thenAnswer(i -> i.getArguments()[0]);

        UsersGetDTO result = usersService.changeUserPassword("NewPassword1!", 1L);

        assertNotNull(result);
        assertTrue(isPasswordMatching("NewPassword1!", result.getPassword()));
    }

    @Test
    void changeUserPassword_InvalidPassword_ThrowsException() {
        Users loggedInUser = new Users();
        loggedInUser.setId(1L);
        loggedInUser.setUsername("john_doe");

        when(usersRepository.findByUsername("john_doe")).thenReturn(loggedInUser);

        Long userId = 1L;
        String invalidPassword = "weak";
        Users existingUser = new Users();
        existingUser.setId(userId);

        when(usersRepository.save(any(Users.class))).thenThrow(new IllegalArgumentException("Password " + invalidPassword + " is not strong enough"));
        when(usersRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usersService.changeUserPassword(invalidPassword, userId);
        });

        assertEquals("Password " + invalidPassword + " is not strong enough", exception.getMessage());
    }

    @Test
    void changeUserInfoVisibility() {
        Users loggedInUser = new Users();
        loggedInUser.setId(1L);
        loggedInUser.setUsername("john_doe");

        when(usersRepository.findByUsername("john_doe")).thenReturn(loggedInUser);

        Users user = new Users();
        user.setId(1L);
        user.setPrivate(false);

        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(usersRepository.save(any(Users.class))).thenAnswer(i -> i.getArguments()[0]);

        UsersGetDTO result = usersService.changeUserInfoVisibility(true, 1L);

        assertNotNull(result);
        assertTrue(result.isPrivate());
    }

    @Test
    void deleteUser_SuccessfulDeletion() {
        doNothing().when(usersRepository).deleteById(1L);

        usersService.deleteUser(1L);

        verify(usersRepository, times(1)).deleteById(1L);
    }

    @Test
    void getUserComments() {
        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setId(1L);
        comments.add(comment);

        when(commentRepository.findByUserId(1L)).thenReturn(Optional.of(comments));

        Optional<List<CommentDTO>> result = usersService.getUserComments(1L);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
    }

    @Test
    void getUserProjects() {
        Project project = new Project();
        project.setId(1L);
        project.setMediaList(new ArrayList<>());
        project.setComments(new ArrayList<>());
        project.setUserLikes(new ArrayList<>());
        project.setFavoriteProjects(new ArrayList<>());
        project.setProjectFollowers(new ArrayList<>());

        Users user = new Users();
        user.setProjects(List.of(project));

        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<List<ProjectDTO>> result = usersService.getUserProjects(1L);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
    }

    @Test
    @WithMockUser(username = "john_doe", password = "newPassword123", roles = {"USER"})
    void setUserPreference() {
        Users loggedInUser = new Users();
        loggedInUser.setId(1L);
        loggedInUser.setUsername("john_doe");

        when(usersRepository.findByUsername("john_doe")).thenReturn(loggedInUser);

        Users user = new Users();
        user.setId(1L);

        Category category = new Category();
        category.setId(1L);

        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(usersRepository.save(any(Users.class))).thenAnswer(i -> i.getArguments()[0]);

        UsersGetDTO result = usersService.setUserPreference(List.of(1L), 1L);

        assertNotNull(result);
        assertEquals(1, result.getUserPreferences().size());
    }
}
