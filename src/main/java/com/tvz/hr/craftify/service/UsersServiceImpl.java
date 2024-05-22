package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.*;
import com.tvz.hr.craftify.repository.CategoryRepository;
import com.tvz.hr.craftify.repository.CommentRepository;
import com.tvz.hr.craftify.repository.ProjectRepository;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.dto.UsersGetDTO;
import com.tvz.hr.craftify.service.dto.*;
import com.tvz.hr.craftify.utilities.exceptions.ApplicationException;
import com.tvz.hr.craftify.utilities.exceptions.DatabaseOperationException;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tvz.hr.craftify.service.PasswordService.*;
import static com.tvz.hr.craftify.utilities.MapToDTOHelper.mapToUsersGetDTO;

@Service
@AllArgsConstructor
public class UsersServiceImpl implements UsersService{
    private CommentRepository commentRepository;
    private UsersRepository usersRepository;
    private ProjectRepository projectRepository;;
    private CategoryRepository categoryRepository;

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
    public UsersGetDTO authenticateUser(LoginDTO login) {
        Optional<Users> optionalUser = usersRepository.getUsersByUsernameOrEmailIgnoreCase(login.getUsernameOrEmail(), login.getUsernameOrEmail());

        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();

            if (isPasswordMatching(login.getPassword(), user.getPassword())) {
                return mapToUsersGetDTO(user);
            } else {
                throw new IllegalArgumentException("Invalid password for user " + login.getUsernameOrEmail());
            }
        } else {
            throw new IllegalArgumentException("User not found: " + login.getUsernameOrEmail());
        }
    }

    @Override
    public UsersGetDTO createUser(UsersPutPostDTO user) {
        List<Long> categoryIds = user.getUserPreferences().stream().distinct().toList();
        List<Category> categories = categoryIds.stream()
                .map(categoryId -> categoryRepository.findById(categoryId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        if (!isPasswordStrong(user.getPassword())) {
            throw new IllegalArgumentException("Password is not strong enough");
        }

        String hashedPassword = hashPassword(user.getPassword());

        Users newUser = new Users(user.getName(), user.getUsername(),user.getEmail(),hashedPassword, user.isAdmin(), user.isPrivate(), categories);
        return mapToUsersGetDTO(usersRepository.save(newUser));
    };
    @Override
    public UsersGetDTO updateUser(UsersPutPostDTO user, Long id) {
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
                    .map(Optional::get)
                    .collect(Collectors.toList());
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
        Users existingUser = usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));

        if (!isPasswordStrong(newPassword)) {
            throw new IllegalArgumentException("Password "+ newPassword + " is not strong enough");
        }
        String newHashedPassword = hashPassword(newPassword);
        existingUser.setPassword(newHashedPassword);
        return mapToUsersGetDTO(usersRepository.save(existingUser));
    }

    @Override
    public UsersGetDTO changeUserInfoVisibility(boolean isPrivate, Long id){
        Users existingUser = usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));

        existingUser.setPrivate(isPrivate);
        return mapToUsersGetDTO(usersRepository.save(existingUser));
    }

    @Override
    public void deleteUser(Long id) { usersRepository.deleteById(id); }

    @Override
    public Optional<List<CommentDTO>> getUserComments(Long id) {
        Optional<List<Comment>> comments = commentRepository.findByUserId(id);
        return comments.map(commentList -> commentList.stream()
                .map(MapToDTOHelper::mapToCommentDTO)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<List<ProjectDTO>> getFavoriteProjects(Long userId) {
        Optional<Users> userOptional = usersRepository.findById(userId);
        return userOptional.map(user -> user.getFavoriteProjects().stream()
                .map(MapToDTOHelper::mapToProjectDTO)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<List<ProjectDTO>> getLikedProjects(Long userId) {
        Optional<Users> userOptional = usersRepository.findById(userId);
        return userOptional.map(user -> user.getLikedProjects().stream()
                .map(MapToDTOHelper::mapToProjectDTO)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<List<ProjectDTO>> getUserProjects(Long userId) {
        Optional<Users> userOptional = usersRepository.findById(userId);
        return userOptional.map(user -> user.getProjects().stream()
                .map(MapToDTOHelper::mapToProjectDTO)
                .collect(Collectors.toList()));
    }
    @Override
    public UsersGetDTO setUserPreference(List<Long> categoryIds, Long userId){
        Users existingUser = usersRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        List<Category> categories = new ArrayList<>();
        categoryIds = categoryIds.stream().distinct().toList();
        if(!categoryIds.isEmpty()) {
            categories = categoryIds.stream()
                    .map(categoryId -> categoryRepository.findById(categoryId))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }
        existingUser.setUserPreferences(categories);
        return mapToUsersGetDTO(usersRepository.save(existingUser));
    }
}
