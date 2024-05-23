package com.tvz.hr.craftify.service.dto;

import com.tvz.hr.craftify.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO{
    private Long id;
    private String name;
}
