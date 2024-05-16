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
public class Tutorial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "complexity_id", nullable = false)
    private Complexity complexity;

    //Images/Videos
    @OneToMany(mappedBy = "tutorial")
    private List<Media> mediaList;


    public Tutorial(Long id, String title, String content, Users user, Category category, Complexity complexity) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
        this.category = category;
        this.complexity = complexity;
    }
}
