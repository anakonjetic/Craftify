package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Category;
import com.tvz.hr.craftify.model.Users;
import com.tvz.hr.craftify.repository.CategoryRepository;
import com.tvz.hr.craftify.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private UsersRepository usersRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getCategoryById(long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category, long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category categoryToUpdate = optionalCategory.get();
            categoryToUpdate.setName(category.getName());
            return categoryRepository.save(categoryToUpdate);
        }
        else{
            throw new RuntimeException("Category with ID: " + id + " not found");
        }
    }

    @Override
    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDTO addUserPreference(Long categoryId, Long userID) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        Users user = usersRepository.findById(userID).orElse(null);
        if (category != null && user != null) {
            category.getUserPreferences().add(user);
            category = categoryRepository.save(category);
            categoryRepository.flush();
        }
        return convertToDTO(category);
    }

    @Override
    public CategoryDTO removeUserPreference(Long categoryId, Long userID) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        Users user = usersRepository.findById(userID).orElse(null);

        if (category != null && user != null) {
            category.getUserPreferences().remove(user);
            category = categoryRepository.save(category);
            categoryRepository.flush();
        }
        return convertToDTO(category);
    }
    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }


}
