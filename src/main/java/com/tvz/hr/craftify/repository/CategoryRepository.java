package com.tvz.hr.craftify.repository;
import com.tvz.hr.craftify.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c.id FROM Category c JOIN c.userPreferences u WHERE u.id = :userId")
    Optional<List<Long>> findPreferredCategoryIdsByUserId(@Param("userId") Long userId);
    }
