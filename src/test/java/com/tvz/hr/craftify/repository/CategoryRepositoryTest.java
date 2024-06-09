package com.tvz.hr.craftify.repository;

import com.tvz.hr.craftify.model.Category;
import com.tvz.hr.craftify.model.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Sql(scripts = "/data.sql")
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void findPreferredCategoryIdsByUserId_UserWithPreferences_ReturnsCategoryIds() {
        Long userId = 2L;
        Optional<List<Long>> result = categoryRepository.findPreferredCategoryIdsByUserId(userId);

        assertTrue(result.isPresent());
        List<Long> categoryIds = result.get();
        assertEquals(2, categoryIds.size());
        assertTrue(categoryIds.contains(1L));
    }

    @Test

    public void findPreferredCategoryIdsByUserId_UserWithoutPreferences_ReturnsEmptyList() {
        Long userId = 3L;
        Optional<List<Long>> result = categoryRepository.findPreferredCategoryIdsByUserId(userId);

        assertTrue(result.isPresent());
        assertTrue(result.get().isEmpty());
    }

    @Test
    public void findPreferredCategoryIdsByUserId_NonExistingUser_ReturnsEmptyOptional() {
        Long userId = 5L;
        Optional<List<Long>> result = categoryRepository.findPreferredCategoryIdsByUserId(userId);
        System.out.println(result);

        assertTrue(result.get().isEmpty());
    }
}
