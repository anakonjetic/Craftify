package com.tvz.hr.craftify.service;
import com.tvz.hr.craftify.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategories();
    Optional<Category> getCategoryById(long id);
    Category createCategory(Category category);
    Category updateCategory(Category category, long id);
    void deleteCategory(long id);
}
