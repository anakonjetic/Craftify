package com.tvz.hr.craftify.repository;
import com.tvz.hr.craftify.model.Category;
import com.tvz.hr.craftify.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    }
