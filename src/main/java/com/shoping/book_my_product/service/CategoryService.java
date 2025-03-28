package com.shoping.book_my_product.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.shoping.book_my_product.entity.Category;


public interface CategoryService {
	public Category saveCategory(Category category);
	public Boolean existCategory(String name);
	public List<Category> getAllCategory();
	public Boolean deleteCategory(Integer id);
	public Category getCategory(Integer id);
	public List<Category> getActiveCategories();
	public Page<Category> getAllCategoriesPagination(Integer pageNo,Integer pageSize);
	public List<Category> searchProduct(String ch);
}
