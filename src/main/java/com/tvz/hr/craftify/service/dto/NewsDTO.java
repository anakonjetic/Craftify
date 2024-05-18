package com.tvz.hr.craftify.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsDTO {
    private Long id;
    private String title;
    private String content;
    private CategoryDTO category;
    private String imageUrl;
}