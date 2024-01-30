package com.security.project.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

//repository interacts with database.
public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByEmail(String email);
}
