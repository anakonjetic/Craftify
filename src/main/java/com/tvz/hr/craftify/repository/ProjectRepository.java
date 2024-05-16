package com.tvz.hr.craftify.repository;
import com.tvz.hr.craftify.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project getById(Long id);

    List<Project> findByCategory_Id(Long id);
    List<Project> findByComplexity_Id(Long id);
    List<Project> findByUser_id(Long id);
}
