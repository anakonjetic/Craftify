package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Users;
import com.tvz.hr.craftify.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserDetailsServiceTest {
    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    public void setUp(){ MockitoAnnotations.openMocks(this); }

    @Test
    void loadUserByUsername_UserExists() {
        Users user = new Users();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setAdmin(false);

        when(usersRepository.getFirstByUsernameOrEmailIgnoreCase("testuser", "testuser"))
                .thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER")));
        assertFalse(userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void loadUserByUsername_UserIsAdmin() {
        Users user = new Users();
        user.setUsername("adminuser");
        user.setEmail("admin@example.com");
        user.setPassword("password");
        user.setAdmin(true);

        when(usersRepository.getFirstByUsernameOrEmailIgnoreCase("adminuser", "adminuser"))
                .thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("adminuser");

        assertNotNull(userDetails);
        assertEquals("adminuser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_UserDoesNotExist() {
        when(usersRepository.getFirstByUsernameOrEmailIgnoreCase("nonexistentuser", "nonexistentuser"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistentuser");
        });
    }
}
