package com.shoping.book_my_product.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.shoping.book_my_product.entity.Product;

public interface ProductService {
	public Product saveProduct(Product product);
	public List<Product> getAllProducts();
	public Boolean deleteProduct(Integer id);
	public Product getProduct(Integer id);
	public Product updateProduct(Product product,MultipartFile file);
	public List<Product> getAllActiveProducts(String category);
	public List<Product> searchProduct(String ch);
	public Page<Product> getAllActiveProductsPagination(Integer pageNo,Integer pageSize,String category);
	public Page<Product> getAllProductsPagination(Integer pageNo,Integer pageSize);
	public Page<Product> searchActiveProductPagination(Integer pageNo, Integer pageSize,String category, String ch);
}
