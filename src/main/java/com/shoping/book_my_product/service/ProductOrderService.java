package com.shoping.book_my_product.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.shoping.book_my_product.dto.OrderRequestDto;
import com.shoping.book_my_product.entity.ProductOrder;

public interface ProductOrderService {
	public void saveOrder(Integer userId,OrderRequestDto orRequestDto) throws Exception;
	public List<ProductOrder> getAllOrderedProducts(Integer userId);
	public ProductOrder updateOrderStatus(Integer id,String status);
	public List<ProductOrder> getAllOrders();
	public ProductOrder searchOrder(String orderId);
	public Page<ProductOrder> getAllOrderedProductsPagination(Integer pageNo,Integer pageSize);
}
