package com.tvz.hr.craftify.repository;
import com.tvz.hr.craftify.model.Complexity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplexityRepository extends JpaRepository<Complexity, Long> {
}
