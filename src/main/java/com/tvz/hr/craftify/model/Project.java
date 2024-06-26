package com.tvz.hr.craftify.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(nullable = false)
    private String title, description;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "complexity_id")
    private Complexity complexity;

    //Images/Videos
    @OneToMany(mappedBy = "project",cascade=ALL)
    private List<Media> mediaList;

    @ManyToMany(targetEntity = Users.class, mappedBy = "likedProjects")
    private List<Users> userLikes;

    @ManyToMany(targetEntity = Users.class, mappedBy = "favoriteProjects")
    private List<Users> favoriteProjects;

    @ManyToMany(targetEntity = Users.class, mappedBy = "followingProjects")
    private List<Users> projectFollowers;

    @OneToMany(mappedBy = "project", cascade=ALL)
    private List<Comment> comments;

    @Override
    public String toString() {
        return "Project{id=" + id + ", title='" + title + '\'' + ", description='" + description + '\'' + ", content='" + content + '\'' + '}';
    }

    public Project(Long id, String title, String description, String content, Users user, Category category, Complexity complexity){
        this.id = id;
        this.title = title;
        this.description = description;
        this.content = content;
        this.user = user;
        this.category = category;
        this.complexity = complexity;
    }
}
