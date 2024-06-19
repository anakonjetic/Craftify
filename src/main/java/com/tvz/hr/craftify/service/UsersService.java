package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Users;
import com.tvz.hr.craftify.service.dto.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface UsersService {
    List<UsersGetDTO> getAllUsers();
    Optional<UsersGetDTO> getUser(Long id);
    UserDTO getUserByUsername(String username);
    UsersGetDTO createUser(UsersPutPostDTO user);
    UsersGetDTO updateUser(UsersPutDTO user, Long id);
    void deleteUser(Long id);
}
