package com.shoping.book_my_product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoping.book_my_product.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	public Boolean existsByName(String name);

	public List<Category> findByIsActiveTrue();
}
