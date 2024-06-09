package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.*;
import com.tvz.hr.craftify.repository.CategoryRepository;
import com.tvz.hr.craftify.repository.CommentRepository;
import com.tvz.hr.craftify.repository.ProjectRepository;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.dto.UsersGetDTO;
import com.tvz.hr.craftify.service.dto.*;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tvz.hr.craftify.service.PasswordService.*;
import static com.tvz.hr.craftify.utilities.MapToDTOHelper.mapToUserDTO;
import static com.tvz.hr.craftify.utilities.MapToDTOHelper.mapToUsersGetDTO;

@Service
@AllArgsConstructor
public class UsersServiceImpl implements UsersService{
    private CommentRepository commentRepository;
    private UsersRepository usersRepository;
    private UserDetailsServiceImpl userDetailsService;
    private CategoryRepository categoryRepository;
    private ProjectRepository projectRepository;

    @Override
    public List<UsersGetDTO> getAllUsers() {
        List<Users> users = usersRepository.findAll();
        return users.stream()
                .map(MapToDTOHelper::mapToUsersGetDTO)
                .collect(Collectors.toList());}
    ;
    @Override
    public Optional<UsersGetDTO> getUser(Long id) {
        Optional<Users> users = usersRepository.findById(id);
        return users.map(MapToDTOHelper::mapToUsersGetDTO);
    };
    @Override
    public Users getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails user){
            return usersRepository.findByUsername(user.getUsername());
        }
        return null;
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        return mapToUserDTO(usersRepository.findByUsername(username));
    }

    @Override
    public void checkAuthorization(Long userId) {
        Users activeUser = getLoggedInUser();
        if (!(Objects.equals(activeUser.getId(), userId) || activeUser.isAdmin())) {
            throw new RuntimeException("You are not authorized to perform this action.");
        }
    }

    @Override
    public UsersGetDTO createUser(UsersPutPostDTO user) {
        List<Long> categoryIds = user.getUserPreferences().stream().distinct().toList();
        List<Category> categories = categoryIds.stream()
                .map(categoryId -> categoryRepository.findById(categoryId))
                .filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toList());

        if (!isPasswordStrong(user.getPassword())) {
            throw new IllegalArgumentException("Password is not strong enough");
        }

        String hashedPassword = hashPassword(user.getPassword());
        Users newUser = new Users(user.getName(), user.getUsername(),user.getEmail(),hashedPassword, user.isAdmin(), user.isPrivate(), categories);
        return mapToUsersGetDTO(usersRepository.save(newUser));
    };
    @Override
    public UsersGetDTO updateUser(UsersPutPostDTO user, Long id) {
        checkAuthorization(id);
        Optional<Users> optionalUser = usersRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return null;
        }
        Users existingUser = optionalUser.get();

        List<Category> categories = existingUser.getUserPreferences();
        List<Long> categoryIds = user.getUserPreferences().stream().distinct().toList();
        if(!categoryIds.isEmpty()) {
            categories = categoryIds.stream()
                    .map(categoryId -> categoryRepository.findById(categoryId))
                    .filter(Optional::isPresent)
                    .map(Optional::get).collect(Collectors.toList());
        }
        if(!user.getPassword().isEmpty()) {
            if (!isPasswordStrong(user.getPassword())) {
                throw new IllegalArgumentException("Password is not strong enough");
            }
            String newPassword = hashPassword(user.getPassword());
            existingUser.setPassword(newPassword);
        }
        existingUser.setName(user.getName());
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setAdmin(user.isAdmin());
        existingUser.setPrivate(user.isPrivate());
        existingUser.setUserPreferences(categories);

        return mapToUsersGetDTO(usersRepository.save(existingUser));
    };

    @Override
    public UsersGetDTO changeUserPassword(String newPassword, Long id){
        checkAuthorization(id);
        Users existingUser = usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));

        if (!isPasswordStrong(newPassword))
            throw new IllegalArgumentException("Password "+ newPassword + " is not strong enough");
        String newHashedPassword = hashPassword(newPassword);
        existingUser.setPassword(newHashedPassword);
        return mapToUsersGetDTO(usersRepository.save(existingUser));
    }

    @Override
    public UsersGetDTO changeUserInfoVisibility(boolean isPrivate, Long id){
        checkAuthorization(id);
        Users existingUser = usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
        existingUser.setPrivate(isPrivate);
        return mapToUsersGetDTO(usersRepository.save(existingUser));
    }

    @Override
    public void deleteUser(Long id) {
        try {
            usersRepository.deleteProjectSubscribersByUserId(id);
            usersRepository.deleteUserProjectLikesByProjectUserId(id);
            usersRepository.deleteFavoritesByProjectUserId(id);
            usersRepository.deleteProjectSubscribersByUserIdOnly(id);
            usersRepository.deleteUserProjectLikesByUserId(id);
            usersRepository.deleteUserSubscribersByUserId(id);
            usersRepository.deleteChildCommentsByProjectUserId(id);
            usersRepository.deleteCommentsByProjectUserId(id);
            usersRepository.deleteMediaProjectsByUserId(id);
            usersRepository.deleteProjectsByUserId(id);
            usersRepository.deleteUserPreferencesByUserId(id);
            usersRepository.deleteFavoritesByUserId(id);
            usersRepository.deleteRefreshTokensByUserId(id);
            usersRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new DataAccessException("Error occurred while deleting user with id: " + id, e) {};
        }
    }

    @Override
    public Optional<List<CommentDTO>> getUserComments(Long id) {
        Optional<List<Comment>> comments = commentRepository.findByUserId(id);
        return comments.map(commentList -> commentList.stream()
                .map(MapToDTOHelper::mapToCommentDTO).collect(Collectors.toList()));
    }

    @Override
    public Optional<List<ProjectDTO>> getUserProjects(Long userId) {
        Optional<Users> userOptional = usersRepository.findById(userId);
        return userOptional.map(user -> user.getProjects().stream()
                .map(MapToDTOHelper::mapToProjectDTO).collect(Collectors.toList()));
    }
    @Override
    public UsersGetDTO setUserPreference(List<Long> categoryIds, Long userId){
        checkAuthorization(userId);
        Users existingUser = usersRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        List<Category> categories = new ArrayList<>();
        categoryIds = categoryIds.stream().distinct().toList();
        if(!categoryIds.isEmpty()) {
            categories = categoryIds.stream()
                    .map(categoryId -> categoryRepository.findById(categoryId))
                    .filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
        }
        existingUser.setUserPreferences(categories);
        return mapToUsersGetDTO(usersRepository.save(existingUser));
    }
}
