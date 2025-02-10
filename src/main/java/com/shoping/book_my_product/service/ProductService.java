package com.shoping.book_my_product.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.shoping.book_my_product.entity.Product;

public interface ProductService {
	public Product saveProduct(Product product);
	public List<Product> getAllProducts();
	public Boolean deleteProduct(long id);
	public Product getProduct(long id);
	public Product updateProduct(Product product,MultipartFile file);
	public List<Product> getAllActiveProducts(String category);
}
