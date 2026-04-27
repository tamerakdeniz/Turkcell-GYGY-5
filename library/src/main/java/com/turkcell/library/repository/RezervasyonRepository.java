package com.turkcell.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.turkcell.library.entity.Rezervasyon;

public interface RezervasyonRepository extends JpaRepository<Rezervasyon, Long> {
}
