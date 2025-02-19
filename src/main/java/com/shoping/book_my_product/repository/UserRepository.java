package com.shoping.book_my_product.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shoping.book_my_product.entity.UserDetails;

public interface UserRepository extends JpaRepository<UserDetails, Integer> {
	public UserDetails findByEmail(String email);

	List<UserDetails> findByRole(String role);
	
	public UserDetails findByResetToken(String token);
	
	public List<UserDetails> findByEmailContainingIgnoreCaseAndRole(String ch,String role);
	
	public Page<UserDetails> findByRole(Pageable pageable,String role);

	public Boolean existsByEmail(String email);
}
