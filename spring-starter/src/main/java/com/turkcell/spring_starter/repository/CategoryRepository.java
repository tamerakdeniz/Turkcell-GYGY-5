package com.turkcell.spring_starter.repository;

import java.util.Set;
import java.util.UUID;

import com.turkcell.spring_starter.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Query("SELECT c FROM Category c WHERE c.name = :query")
    Set<Category> findByName(String query);
    
}
