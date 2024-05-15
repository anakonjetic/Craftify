package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Comment;
import com.tvz.hr.craftify.model.Project;
import com.tvz.hr.craftify.model.Users;
import com.tvz.hr.craftify.model.Category;
import com.tvz.hr.craftify.repository.ProjectRepository;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.request.UsersRequest;
import com.tvz.hr.craftify.service.dto.CategoryDTO;
import com.tvz.hr.craftify.service.dto.CommentDTO;
import com.tvz.hr.craftify.service.dto.ProjectDTO;
import com.tvz.hr.craftify.service.dto.UserDTO;
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
    //public List<UsersRequest> getAllUsers() { return usersRepository.getAllUsers(); };
    public List<UsersRequest> getAllUsers() {
        List<Users> users = usersRepository.findAll();
        return users.stream()
                .map(this::mapToUsersRequest)
                .collect(Collectors.toList());}
    ;
    public Optional<UsersRequest> getUser(Long id) { return usersRepository.getUserById(id); };
    public UsersRequest createUser(UsersRequest user) {
        List<Category> categories = user.getUserPreferences().stream()
                .map(MapToDTOHelper::mapToCategory)
                .collect(Collectors.toList());
        Users newUser = new Users(user.getUsername(),user.getEmail(),user.getPassword(), user.isAdmin(), categories);
        return mapToUsersRequest(usersRepository.save(newUser));
    };
    public UsersRequest updateUser(UsersRequest user, Long id) {
        Optional<Users> optionalUser = usersRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return null;
        }

        Users existingUser = optionalUser.get();

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setAdmin(user.isAdmin());

        return mapToUsersRequest(usersRepository.save(existingUser));

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


    private UsersRequest mapToUsersRequest(Users user) {
        /*List<String> categoryNames = user.getUserPreferences().stream()
                .map(Category::getName)
                .collect(Collectors.toList());*/
        List<CategoryDTO> category = user.getUserPreferences().stream()
                .map(MapToDTOHelper::mapToCategoryDTO)
                .collect(Collectors.toList());

        return new UsersRequest(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.isAdmin(),
                category
        );
    }


}
