package com.turkcell.spring_cqrs.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.turkcell.spring_cqrs.domain.User;

public interface UserRepository extends JpaRepository<User, UUID> {}