package com.turkcell.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.turkcell.library.entity.Kitap;

public interface KitapRepository extends JpaRepository<Kitap, Long> {
}
