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
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_preferences",
            joinColumns = @JoinColumn (name = "category_id"),
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private List<Users> userPreferences;
}
