package com.tvz.hr.craftify.service.dto;

import com.tvz.hr.craftify.model.Category;
import com.tvz.hr.craftify.model.Complexity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TutorialDTO {
    private Long id;
    private String title;
    private String content;
    private CategoryDTO category;
    private ComplexityDTO complexity;
}
