package com.turkcell.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.turkcell.library.entity.KitapKopya;

public interface KitapKopyaRepository extends JpaRepository<KitapKopya, Long> {
}
