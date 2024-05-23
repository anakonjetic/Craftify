package com.tvz.hr.craftify.controller;

import com.tvz.hr.craftify.model.Category;
import com.tvz.hr.craftify.service.CategoryService;
import com.tvz.hr.craftify.service.ProjectService;
import com.tvz.hr.craftify.service.dto.*;
import com.tvz.hr.craftify.utilities.exceptions.ApplicationException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/category")
@AllArgsConstructor
@CrossOrigin(origins = {"http://test-craftify.vercel.app", "http://localhost:4200"})
public class CategoryController {

    private CategoryService categoryService;
    private ProjectService projectService;
    @GetMapping("/all")
    public List<CategoryDTO> getCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryGetDTO> getCategory(@PathVariable long id) {
        Optional<CategoryGetDTO> categoryOptional = categoryService.getCategory(id);
        return categoryOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping
    public ResponseEntity<CategoryGetDTO> createCategory(@RequestBody CategoryPostPutDTO category) {
        return new ResponseEntity<>(
                categoryService.createCategory(category), HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryGetDTO> updateCategory(@PathVariable long id, @RequestBody CategoryPostPutDTO category) {
        try{
            CategoryGetDTO updateCategory = categoryService.updateCategory(category, id);
            return new ResponseEntity<>(updateCategory, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/preference")
    public ResponseEntity<Void> addUserPreference(@RequestBody List<UserPreferencesDTO> userPreferencesDTO) {
        userPreferencesDTO.forEach(
                u -> categoryService.addUserPreference(u.getCategoryId(), u.getUserId()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/preference")
    public ResponseEntity<Void> removeUserPreference(@RequestBody UserPreferencesDTO userPreferencesDTO) {
        categoryService.removeUserPreference(userPreferencesDTO.getCategoryId(), userPreferencesDTO.getUserId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/preference/{userId}")
    public ResponseEntity<List<ProjectGetDTO>> getPreferredProjects(@PathVariable long userId) {
        try {
            Optional<List<CategoryDTO>> categoryDTOS = categoryService.getUserPreferences(userId);

            List<ProjectGetDTO> projectGetDTOS = new ArrayList<>();

            if (categoryDTOS.isPresent()) {
                for (CategoryDTO categoryDTO : categoryDTOS.get()) {
                    Optional<List<ProjectGetDTO>> categoryProjects = projectService.getProjectsByCategory(categoryDTO.getId());
                    categoryProjects.ifPresent(projectGetDTOS::addAll);
                }
            }

            return ResponseEntity.ok(projectGetDTOS);
        } catch (ApplicationException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
