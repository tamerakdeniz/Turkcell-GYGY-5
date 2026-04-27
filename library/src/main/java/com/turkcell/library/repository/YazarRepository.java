package com.turkcell.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.turkcell.library.entity.Yazar;

public interface YazarRepository extends JpaRepository<Yazar, Long> {
}
