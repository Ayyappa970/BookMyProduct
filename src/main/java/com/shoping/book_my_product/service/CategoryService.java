package com.shoping.book_my_product.service;

import java.util.List;

import com.shoping.book_my_product.entity.Category;

public interface CategoryService {
	public Category saveCategory(Category category);
	public Boolean existCategory(String name);
	public List<Category> getAllCategory();
	public Boolean deleteCategory(long id);
	public Category getCategory(long id);
	public List<Category> getActiveCategories();
}
