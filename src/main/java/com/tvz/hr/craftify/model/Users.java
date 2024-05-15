package com.tvz.hr.craftify.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
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

    //TODO quick fix, remove hahaha
    public Users(long l, String johnDoe) {
        this.id = l;
        this.username = johnDoe;
    }
}
