package com.turkcell.spring_starter.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.turkcell.spring_starter.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, UUID> {

}
