package com.tvz.hr.craftify.repository;

import com.tvz.hr.craftify.model.Category;
import com.tvz.hr.craftify.model.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository  extends JpaRepository<News, Long> {
    List<News> findByCategoryId(Long categoryId);
}
