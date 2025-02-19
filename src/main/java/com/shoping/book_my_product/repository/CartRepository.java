package com.shoping.book_my_product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoping.book_my_product.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer>{
	public Cart findByProductIdAndUserUserId(Integer productId,Integer userId);

	public long countByUserUserId(Integer userId);

	public List<Cart> findByUserUserId(Integer userId);
}
