package com.shoping.book_my_product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoping.book_my_product.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{
	public Cart findByProductIdAndUserUserId(long productId,long userId);

	public long countByUserUserId(long userId);

	public List<Cart> findByUserUserId(long userId);
}
