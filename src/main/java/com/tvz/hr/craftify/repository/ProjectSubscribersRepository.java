package com.tvz.hr.craftify.repository;

import com.tvz.hr.craftify.model.ProjectSubscribers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectSubscribersRepository extends JpaRepository<ProjectSubscribers, Long> {
}
