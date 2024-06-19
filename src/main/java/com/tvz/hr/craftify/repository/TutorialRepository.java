package com.tvz.hr.craftify.repository;

import com.tvz.hr.craftify.model.Category;
import com.tvz.hr.craftify.model.Complexity;
import com.tvz.hr.craftify.model.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TutorialRepository  extends JpaRepository<Tutorial, Long> {
    List<Tutorial> findByCategory(Category category);
    List<Tutorial> findByComplexity(Complexity complexity);
    List<Tutorial> findByCategory_Id(Long id);
}
