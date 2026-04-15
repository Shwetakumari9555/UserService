package com.UserService.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.UserService.entities.User;

public interface UserRepository extends JpaRepository<User, UUID> {
	
	Optional<User> findByEmail(String email);
	boolean existsByEmail(String email);

}
