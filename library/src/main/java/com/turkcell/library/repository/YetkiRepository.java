package com.turkcell.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.turkcell.library.entity.Yetki;

public interface YetkiRepository extends JpaRepository<Yetki, Long> {
}
