package com.shoping.book_my_product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoping.book_my_product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	public List<Product> findByIsActiveTrue();

	List<Product> findByCategory(String category);

}
