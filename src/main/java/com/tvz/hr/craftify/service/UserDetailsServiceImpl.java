package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Users;
import com.tvz.hr.craftify.repository.UsersRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tvz.hr.craftify.service.PasswordService.isPasswordMatching;
import static com.tvz.hr.craftify.utilities.MapToDTOHelper.mapToUsersGetDTO;
@Configuration
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Users user = usersRepository.getFirstByUsernameOrEmailIgnoreCase(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Unknown user " + usernameOrEmail));

        String[] roles = user.isAdmin() ? new String[]{"USER", "ADMIN"} : new String[]{"USER"};

        List<SimpleGrantedAuthority> authorities = user.isAdmin() ?
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER")) :
                List.of(new SimpleGrantedAuthority("ROLE_USER"));

        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .roles(roles)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
