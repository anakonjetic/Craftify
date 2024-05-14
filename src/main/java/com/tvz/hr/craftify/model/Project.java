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
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    private String title;
    private String description;
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "complexity_id")
    private Complexity complexity;

    @OneToMany(mappedBy = "project")
    private List<Media> mediaList;

    @ManyToMany(targetEntity = Users.class, mappedBy = "likedProjects")
    private List<Users> userLikes;

    @ManyToMany(targetEntity = Users.class, mappedBy = "favoriteProjects")
    private List<Users> favoriteProjects;

    @ManyToMany(targetEntity = Users.class, mappedBy = "followingProjects")
    private List<Users> projectFollowers;

    @OneToMany(mappedBy = "project")
    private List<Comment> comments;
}
