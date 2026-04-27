package com.turkcell.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.turkcell.library.entity.Ogrenci;

public interface OgrenciRepository extends JpaRepository<Ogrenci, Long> {
}
