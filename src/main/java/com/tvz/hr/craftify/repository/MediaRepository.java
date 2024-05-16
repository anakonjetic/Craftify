package com.tvz.hr.craftify.repository;

import com.tvz.hr.craftify.model.Media;
import com.tvz.hr.craftify.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
}
