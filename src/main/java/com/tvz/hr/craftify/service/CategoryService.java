package com.tvz.hr.craftify.service;
import com.tvz.hr.craftify.model.Category;
import com.tvz.hr.craftify.service.dto.CategoryDTO;
import com.tvz.hr.craftify.service.dto.CategoryGetDTO;
import com.tvz.hr.craftify.service.dto.CategoryPostPutDTO;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();
    Optional<CategoryGetDTO> getCategory(long id);
    Optional<Category> getCategoryById(long id);
    CategoryGetDTO createCategory(CategoryPostPutDTO category);
    CategoryGetDTO updateCategory(CategoryPostPutDTO category, long id);
    void deleteCategory(long id);
    CategoryDTO addUserPreference(Long categoryId, Long userID);
    CategoryDTO removeUserPreference(Long categoryId, Long userID);
    Optional<List<CategoryDTO>> getUserPreferences(Long userId);
}
