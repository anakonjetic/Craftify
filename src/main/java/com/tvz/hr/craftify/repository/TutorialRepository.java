package com.tvz.hr.craftify.repository;
import com.tvz.hr.craftify.model.Category;
import com.tvz.hr.craftify.model.Complexity;
import com.tvz.hr.craftify.model.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TutorialRepository  extends JpaRepository<Tutorial, Long> {
    List<Tutorial> findByCategory(Category category);
    List<Tutorial> findByComplexity(Complexity complexity);
}
