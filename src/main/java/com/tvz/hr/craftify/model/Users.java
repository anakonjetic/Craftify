package com.tvz.hr.craftify.model;

import com.tvz.hr.craftify.service.dto.UsersGetDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@ToString(exclude = {"userPreferences", "likedProjects", "favoriteProjects", "followers", "followedUsers", "followingProjects", "projects", "comments"})
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    @Column(columnDefinition = "BIT")
    @JoinColumn(name = "is_admin")
    private boolean isAdmin;

    public Users(String username, String email, String password, boolean isAdmin, List<Category> userPreferences) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.userPreferences = userPreferences;
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
            name = "projectSubscribers",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "project_id") }
    )
    private List<Project> followingProjects;

    // Projects created by the current user.
    @OneToMany(mappedBy = "user")
    private List<Project> projects;

    @OneToMany(mappedBy = "user")
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
