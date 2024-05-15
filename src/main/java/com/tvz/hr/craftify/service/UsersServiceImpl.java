package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Comment;
import com.tvz.hr.craftify.model.Project;
import com.tvz.hr.craftify.model.Users;
import com.tvz.hr.craftify.model.Category;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.request.UsersRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsersServiceImpl implements UsersService{
    private UsersRepository usersRepository;
    //public List<UsersRequest> getAllUsers() { return usersRepository.getAllUsers(); };
    public List<UsersRequest> getAllUsers() {
        List<Users> users = usersRepository.findAll();
        return users.stream()
                .map(this::mapToUsersRequest)
                .collect(Collectors.toList());}
    ;
    public Optional<UsersRequest> getUser(Long id) { return usersRepository.getUserById(id); };
    public UsersRequest createUser(UsersRequest user) {
        Users newUser = new Users(user.getUsername(),user.getEmail(),user.getPassword(), user.isAdmin());
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
    public void deleteUser(Long id) { usersRepository.deleteById(id); };

    public List<CommentDTO> getUserComments(Long id){
        List<Comment> comments = usersRepository.getUserComments(id);
        return comments.stream()
                .map(this::mapToCommentDTO)
                .collect(Collectors.toList());
    }

    public List<ProjectDTO> getFavoriteProjects(Long userId){
        List<Project> favoriteProjects = usersRepository.findById(userId).get().getFavoriteProjects();
        return favoriteProjects.stream()
                .map(this::mapToProjectDTO)
                .collect(Collectors.toList());
    }

    public List<ProjectDTO> getLikedProjects(Long userId){
        List<Project> likedProjects = usersRepository.findById(userId).get().getLikedProjects();
        return likedProjects.stream()
                .map(this::mapToProjectDTO)
                .collect(Collectors.toList());
    }

    public List<ProjectDTO> getUserProjects(Long userId){
        List<Project> projects = usersRepository.findById(userId).get().getProjects();
        return projects.stream()
                .map(this::mapToProjectDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getUserFollowers(Long userId){
        List<Users> users = usersRepository.findById(userId).get().getFollowers();
        return users.stream()
                .map(this::mapToUserDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getUserFollowings(Long userId){
        List<Users> users = usersRepository.findById(userId).get().getFollowedUsers();
        return users.stream()
                .map(this::mapToUserDTO)
                .collect(Collectors.toList());
    }

    public List<ProjectDTO> getUserProjectFollowings(Long userId){
        List<Project> projects = usersRepository.findById(userId).get().getFollowingProjects();
        return projects.stream()
                .map(this::mapToProjectDTO)
                .collect(Collectors.toList());
    }


    private UsersRequest mapToUsersRequest(Users user) {
        /*List<String> categoryNames = user.getUserPreferences().stream()
                .map(Category::getName)
                .collect(Collectors.toList());*/
        List<CategoryDTO> category = user.getUserPreferences().stream()
                .map(this::mapToCategoryDTO)
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


    private UserDTO mapToUserDTO(Users user){
        return new UserDTO(
                user.getId(),
                user.getUsername()
        );
    }
    private ProjectDTO mapToProjectDTO(Project project){
        return new ProjectDTO(
                project.getId(),
                project.getUser().getUsername(),
                project.getTitle(),
                project.getDescription(),
                project.getContent(),
                project.getCategory().getName(),
                project.getComplexity().getName(),
                project.getMediaList()
        );
    }
    private CommentDTO mapToCommentDTO (Comment comment){
        return new CommentDTO(
                comment.getId(),
                comment.getComment(),
                comment.getUser().getUsername(),
                comment.getCommentTime()
        );
    }
    private CategoryDTO mapToCategoryDTO(Category category){
        return new CategoryDTO(
                category.getId(),
                category.getName()
        );
    }
}
