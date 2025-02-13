package com.shoping.book_my_product.service;

import java.util.List;

import com.shoping.book_my_product.dto.OrderRequestDto;
import com.shoping.book_my_product.entity.ProductOrder;

public interface ProductOrderService {
	public void saveOrder(long userId,OrderRequestDto orRequestDto) throws Exception;
	public List<ProductOrder> getAllOrderedProducts(long userId);
	public ProductOrder updateOrderStatus(long id,String status);
	public List<ProductOrder> getAllOrders();
	public ProductOrder searchOrder(String orderId);
}
