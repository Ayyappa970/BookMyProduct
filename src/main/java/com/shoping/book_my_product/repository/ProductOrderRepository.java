package com.shoping.book_my_product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoping.book_my_product.entity.ProductOrder;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {

	List<ProductOrder> findByUserUserId(long userId);
	ProductOrder findByOrderId(String orderId);
}
