package com.shoping.book_my_product.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shoping.book_my_product.entity.Product;
import com.shoping.book_my_product.repository.ProductRepository;
import com.shoping.book_my_product.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepo;

	@Override
	public Product saveProduct(Product product) {
		return productRepo.save(product);
	}

	@Override
	public List<Product> getAllProducts() {
		return productRepo.findAll();
	}

	@Override
	public Boolean deleteProduct(Integer id) {
		Product product = productRepo.findById(id).orElse(null);
		if (!ObjectUtils.isEmpty(product)) {
			productRepo.delete(product);
			return true;
		}
		return false;
	}

	@Override
	public Product getProduct(Integer id) {
		Product product = productRepo.findById(id).orElse(null);
		if (!ObjectUtils.isEmpty(product))
			return product;
		return null;
	}

	@Override
	public Product updateProduct(Product product, MultipartFile file) {
		Product oldProduct = getProduct(product.getId());
		String newImage = file.isEmpty() ? oldProduct.getImage() : file.getOriginalFilename();
		oldProduct.setCategory(product.getCategory());
		oldProduct.setDescription(product.getDescription());
		oldProduct.setPName(product.getPName());
		oldProduct.setPrice(product.getPrice());
		oldProduct.setStock(product.getStock());
		oldProduct.setDiscount(product.getDiscount());
		double discount=product.getPrice()*product.getDiscount()/100;
		double discountPrice=product.getPrice()-discount;
		oldProduct.setDiscountPrice(discountPrice);
		oldProduct.setIsActive(product.getIsActive());
		oldProduct.setImage(newImage);
		Product updatedProduct = productRepo.save(oldProduct);
		if (!ObjectUtils.isEmpty(updatedProduct)) {
			if (!file.isEmpty()) {
				try {
					File file2 = new ClassPathResource("static/images").getFile();
					Path path = Paths.get(file2.getAbsolutePath() + File.separator + "product_imgs" + File.separator
							+ file.getOriginalFilename());
					System.out.println(path);
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return product;
		}
		return null;
	}

	@Override
	public List<Product> getAllActiveProducts(String category) {
		List<Product> products=null;
		if(ObjectUtils.isEmpty(category)) {
			products=productRepo.findByIsActiveTrue();
		}else {
			products=productRepo.findByCategory(category);
		}
		return products;
	}

	@Override
	public List<Product> searchProduct(String ch) {
		return productRepo.findBypNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch);
	}

	@Override
	public Page<Product> getAllActiveProductsPagination(Integer pageNo, Integer pageSize,String category) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Product> pageProducts=null;
		if(ObjectUtils.isEmpty(category)) {
			pageProducts=productRepo.findByIsActiveTrue(pageable);
		}else {
			pageProducts=productRepo.findByCategory(pageable,category);
		}
		return pageProducts;
	}

	@Override
	public Page<Product> getAllProductsPagination(Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		return productRepo.findAll(pageable);
	}

	@Override
	public Page<Product> searchActiveProductPagination(Integer pageNo, Integer pageSize,String category,String ch) {
		Page<Product> pageProducts=null;
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		pageProducts=productRepo.findBypNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseAndIsActiveTrue(ch, ch, pageable);
//		if(ObjectUtils.isEmpty(category)) {
//			pageProducts=productRepo.findByIsActiveTrue(pageable);
//		}else {
//			pageProducts=productRepo.findByCategory(pageable,category);
//		}
		return pageProducts;
	}

}
