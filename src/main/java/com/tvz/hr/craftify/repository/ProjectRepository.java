package com.tvz.hr.craftify.repository;
import com.tvz.hr.craftify.model.Project;
import com.tvz.hr.craftify.service.dto.ProjectGetDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project getById(Long id);

    List<Project> findByCategory_Id(Long id);
    List<Project> findByComplexity_Id(Long id);
    List<Project> findByUser_id(Long id);
    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.userLikes ul GROUP BY p ORDER BY SIZE(ul) DESC")
    List<Project> getAllProjectsAndOrderByUserLikes();

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.userLikes ul WHERE p.category.id = :categoryId GROUP BY p ORDER BY SIZE(ul) DESC")
    List<Project> getAllProjectsByCategoryIdAndOrderByUserLikes(@Param("categoryId") Long categoryId);

    @Query("SELECT p FROM Project p JOIN p.user u " +
            "WHERE (:nameOrUser IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :nameOrUser, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :nameOrUser, '%')) " +
            "OR LOWER(u.name) LIKE LOWER(CONCAT('%', :nameOrUser, '%'))) " +
            "AND (:categoryId IS NULL OR p.category.id = :categoryId) " +
            "AND (:complexityId IS NULL OR p.complexity.id = :complexityId)")
    List<Project> findByFilters(@Param("nameOrUser") String nameOrUser,
                                      @Param("categoryId") Long categoryId,
                                      @Param("complexityId") Long complexityId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM favorites WHERE project_id = :id", nativeQuery = true)
    void deleteFavoritesByProjectId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM project_subscribers WHERE project_id = :id", nativeQuery = true)
    void deleteProjectSubscribersByProjectId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_project_likes WHERE project_id = :id", nativeQuery = true)
    void deleteUserProjectLikesByProjectId(@Param("id") Long id);
}
