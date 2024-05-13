package com.tvz.hr.craftify.repository;

import com.tvz.hr.craftify.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavouriteRepository extends JpaRepository<Favorite, Long> {
}
