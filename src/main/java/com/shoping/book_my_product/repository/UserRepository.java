package com.shoping.book_my_product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoping.book_my_product.entity.UserDetails;

public interface UserRepository extends JpaRepository<UserDetails, Long> {
	public UserDetails findByEmail(String email);

	public List<UserDetails> findByRole(String role);
	
	public UserDetails findByResetToken(String token);
	
	public List<UserDetails> findByEmailContainingIgnoreCase(String ch);
}
