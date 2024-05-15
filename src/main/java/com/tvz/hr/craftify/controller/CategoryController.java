package com.tvz.hr.craftify.controller;

import com.tvz.hr.craftify.model.Category;
import com.tvz.hr.craftify.service.CategoryService;
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
    public List<Category> getCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable long id) {
        Optional<Category> categoryOptional = categoryService.getCategoryById(id);
        return categoryOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return new ResponseEntity<>(
                categoryService.createCategory(category), HttpStatus.CREATED
        );
    }
    public ResponseEntity<Category> addUserPreference(@PathVariable Category categoryId, @PathVariable Long userId) {
        Category category = categoryService.addUserPreference(categoryId.getId(), userId);
        return ResponseEntity.ok(category);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable long id, @RequestBody Category category) {
        try{
            Category updateCategory = categoryService.updateCategory(category, id);
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
