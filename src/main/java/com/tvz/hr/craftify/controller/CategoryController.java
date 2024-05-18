package com.tvz.hr.craftify.controller;

import com.tvz.hr.craftify.model.Category;
import com.tvz.hr.craftify.service.CategoryService;
import com.tvz.hr.craftify.service.dto.CategoryDTO;
import com.tvz.hr.craftify.service.dto.CategoryGetDTO;
import com.tvz.hr.craftify.service.dto.CategoryPostPutDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/category")
@AllArgsConstructor
public class CategoryController {

    private CategoryService categoryService;
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
    public ResponseEntity<Category> addUserPreference(@PathVariable Category categoryId, @PathVariable Long userId) {
        Category category = categoryService.addUserPreference(categoryId.getId(), userId);
        return ResponseEntity.ok(category);

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

    @PostMapping("/{categoryId}/user/{userId}")
    public ResponseEntity<Category> addUserPreference(@PathVariable long categoryId, @PathVariable long userId) {
        Category category = categoryService.addUserPreference(categoryId, userId);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("{categoryId}/user/{userId}")
    public ResponseEntity<Category> removeUserPreference(@PathVariable long categoryId, @PathVariable long userId) {
        Category category = categoryService.removeUserPreference(categoryId, userId);
        return ResponseEntity.ok(category);
    }



}
