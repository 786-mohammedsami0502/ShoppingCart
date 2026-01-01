package com.Ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecom.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	
	public User findByEmail(String Email);

	public List<User> findByRole(String role);
	
	public User findByResetToken(String token);
	
	
}
