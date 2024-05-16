package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.*;
import com.tvz.hr.craftify.repository.ProjectRepository;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.dto.UsersGetDTO;
import com.tvz.hr.craftify.service.dto.*;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsersServiceImpl implements UsersService{
    private UsersRepository usersRepository;
    private ProjectRepository projectRepository;
    private CategoryService categoryService;
    //public List<UsersRequest> getAllUsers() { return usersRepository.getAllUsers(); };
    public List<UsersGetDTO> getAllUsers() {
        List<Users> users = usersRepository.findAll();
        return users.stream()
                .map(this::mapToUsersGetDTO)
                .collect(Collectors.toList());}
    ;
    public Optional<UsersGetDTO> getUser(Long id) {
        Optional<Users> users = usersRepository.findById(id);
        return users.map(this::mapToUsersGetDTO);
    };

    public UsersGetDTO createUser(UsersPutPostDTO user) {
        /*List<Category> categories = user.getUserPreferences().stream()
                .map(MapToDTOHelper::mapToCategory)
                .collect(Collectors.toList());*/
        List<Long> categoryIds = user.getUserPreferences();
        List<Category> categories = categoryIds.stream()
                .map(categoryId -> categoryService.getCategoryById(categoryId))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
        Users newUser = new Users(user.getUsername(),user.getEmail(),user.getPassword(), user.isAdmin(), categories);
        return mapToUsersGetDTO(usersRepository.save(newUser));
    };
    public UsersGetDTO updateUser(UsersPutPostDTO user, Long id) {
        Optional<Users> optionalUser = usersRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return null;
        }
        List<Long> categoryIds = user.getUserPreferences();
        List<Category> categories = categoryIds.stream()
                .map(categoryId -> categoryService.getCategoryById(categoryId))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        Users existingUser = optionalUser.get();

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setAdmin(user.isAdmin());
        existingUser.setUserPreferences(categories);

        return mapToUsersGetDTO(usersRepository.save(existingUser));

    };
    public void deleteUser(Long id) { usersRepository.deleteById(id); }


    @Override
    public void addToFavorites(Long userId, Long projectId) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
        user.getFavoriteProjects().add(project);
        usersRepository.save(user);
    }

    @Override
    public void removeFromFavorites(Long userId, Long projectId) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.getFavoriteProjects().removeIf(project -> project.getId().equals(projectId));
        usersRepository.save(user);
    }

    public List<CommentDTO> getUserComments(Long id){
        List<Comment> comments = usersRepository.getUserComments(id);
        return comments.stream()
                .map(MapToDTOHelper::mapToCommentDTO)
                .collect(Collectors.toList());
    }

    public List<ProjectDTO> getFavoriteProjects(Long userId){
        List<Project> favoriteProjects = usersRepository.findById(userId).get().getFavoriteProjects();
        return favoriteProjects.stream()
                .map(MapToDTOHelper::mapToProjectDTO)
                .collect(Collectors.toList());
    }

    public List<ProjectDTO> getLikedProjects(Long userId){
        List<Project> likedProjects = usersRepository.findById(userId).get().getLikedProjects();
        return likedProjects.stream()
                .map(MapToDTOHelper::mapToProjectDTO)
                .collect(Collectors.toList());
    }

    public List<ProjectDTO> getUserProjects(Long userId){
        List<Project> projects = usersRepository.findById(userId).get().getProjects();
        return projects.stream()
                .map(MapToDTOHelper::mapToProjectDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getUserFollowers(Long userId){
        List<Users> users = usersRepository.findById(userId).get().getFollowers();
        return users.stream()
                .map(MapToDTOHelper::mapToUserDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getUserFollowings(Long userId){
        List<Users> users = usersRepository.findById(userId).get().getFollowedUsers();
        return users.stream()
                .map(MapToDTOHelper::mapToUserDTO)
                .collect(Collectors.toList());
    }

    public List<ProjectDTO> getUserProjectFollowings(Long userId){
        List<Project> projects = usersRepository.findById(userId).get().getFollowingProjects();
        return projects.stream()
                .map(MapToDTOHelper::mapToProjectDTO)
                .collect(Collectors.toList());
    }


    private UsersGetDTO mapToUsersGetDTO(Users user) {
        /*List<String> categoryNames = user.getUserPreferences().stream()
                .map(Category::getName)
                .collect(Collectors.toList());*/
        List<CategoryDTO> category = user.getUserPreferences().stream()
                .map(MapToDTOHelper::mapToCategoryDTO)
                .collect(Collectors.toList());

        return new UsersGetDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.isAdmin(),
                category
        );
    }
}
