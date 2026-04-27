package com.turkcell.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.turkcell.library.entity.Kategori;

public interface KategoriRepository extends JpaRepository<Kategori, Long> {
}
