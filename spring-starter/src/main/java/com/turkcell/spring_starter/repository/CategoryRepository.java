package com.turkcell.spring_starter.repository;

import java.util.UUID;

import com.turkcell.spring_starter.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, UUID> {



}
