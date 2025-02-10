package com.shoping.book_my_product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.shoping.book_my_product.entity.Category;
import com.shoping.book_my_product.repository.CategoryRepository;
import com.shoping.book_my_product.service.CategoryService;
@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryRepository categoryRepo;
	
	@Override
	public Category saveCategory(Category category) {
		return categoryRepo.save(category);
	}
	@Override
	public Boolean existCategory(String name) {
		return categoryRepo.existsByName(name);
	}
	@Override
	public List<Category> getAllCategory() {
		return categoryRepo.findAll();
	}
	@Override
	public Boolean deleteCategory(long id) {
		Category category = categoryRepo.findById(id).orElse(null);
		if(!ObjectUtils.isEmpty(category)) {
			categoryRepo.delete(category);
			return true;
		}
		return false;
	}
	@Override
	public Category getCategory(long id) {
		Category category = categoryRepo.findById(id).orElse(null);
		if(category!=null)
			return category;
		return null;
	}
	@Override
	public List<Category> getActiveCategories() {
		return categoryRepo.findByIsActiveTrue();
	}

	

}
