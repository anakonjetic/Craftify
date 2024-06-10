package com.tvz.hr.craftify.model;

import com.tvz.hr.craftify.service.dto.UsersGetDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(columnDefinition = "BIT", nullable = false)
    @JoinColumn(name = "is_admin")
    private boolean isAdmin;

    @Column(columnDefinition = "BIT", nullable = false)
    @JoinColumn(name = "is_private")
    private boolean isPrivate;

    public Users(Long id, String name, String username, String email, String password, boolean isAdmin, boolean isPrivate, List<Category> userPreferences) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.isPrivate = isPrivate;
        this.userPreferences = userPreferences;
    }

    public Users(String name, String username, String email, String password, boolean isAdmin, boolean isPrivate, List<Category> userPreferences) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.isPrivate = isPrivate;
        this.userPreferences = userPreferences;
    }

    public Users(Long id, String name, String username){
        this.id = id;
        this.name = name;
        this.username = username;
    }

    @ManyToMany(targetEntity = Category.class)
    @JoinTable(
            name = "user_preferences",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "category_id") }
    )
    private List<Category> userPreferences;

    //LIKES
    @ManyToMany(targetEntity = Project.class)
    @JoinTable(
            name = "user_project_likes",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "project_id") }
    )
    private List<Project> likedProjects;

    //FAVORITES
    @ManyToMany(targetEntity = Project.class)
    @JoinTable(
            name = "favorites",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "project_id") }
    )
    private List<Project> favoriteProjects;

    //List of users following the current user.
    @ManyToMany(mappedBy = "followedUsers")
    private List<Users> followers;

    //List of users this user follows.
    @ManyToMany
    @JoinTable(
            name = "userSubscribers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "followed_user_id")
    )
    private List<Users> followedUsers;

    //List of projects who this user follows
    @ManyToMany(targetEntity = Project.class)
    @JoinTable(
            name = "project_subscribers",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "project_id") }
    )
    private List<Project> followingProjects;

    // Projects created by the current user.
    @OneToMany(mappedBy = "user")
    private List<Project> projects;

    @OneToMany(mappedBy = "user",cascade=ALL)
    private List<Tutorial> tutorials;

    @OneToMany(mappedBy = "user",cascade=ALL)
    private List<Comment> comments;

    @Override
    public String toString() {
        return "Users{id=" + id + ", username='" + username + '\'' + ", email='" + email + '\'' + ", isAdmin=" + isAdmin + '}';
    }

    public static Users mapToUserFromUserRequest(UsersGetDTO request) {
        Users user = new Users();
        user.setId(request.getId());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setAdmin(request.isAdmin());
        return user;
    }
}
