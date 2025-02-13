package com.shoping.book_my_product.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shoping.book_my_product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	public List<Product> findByIsActiveTrue();

	List<Product> findByCategory(String category);

	List<Product> findBypNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch,String ch2);

	public Page<Product> findByIsActiveTrue(Pageable pageable);

	public Page<Product> findByCategory(Pageable pageable,String category);

	

}
