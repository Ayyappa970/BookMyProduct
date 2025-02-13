package com.shoping.book_my_product.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.shoping.book_my_product.dto.OrderRequestDto;
import com.shoping.book_my_product.entity.Cart;
import com.shoping.book_my_product.entity.OrderAddress;
import com.shoping.book_my_product.entity.ProductOrder;
import com.shoping.book_my_product.repository.CartRepository;
import com.shoping.book_my_product.repository.ProductOrderRepository;
import com.shoping.book_my_product.service.ProductOrderService;
import com.shoping.book_my_product.util.CommonUtil;
import com.shoping.book_my_product.util.OrderStatus;
@Service
public class PrductOrderServiceImpl implements ProductOrderService {
	@Autowired
	private ProductOrderRepository orderRepo;
	
	@Autowired
	private CartRepository cartRepo;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Override
	public void saveOrder(long userId,OrderRequestDto orRequestDto) throws Exception {
		List<Cart> carts = cartRepo.findByUserUserId(userId);
		for(Cart cart:carts) {
			ProductOrder order=new ProductOrder();
			order.setOrderId(UUID.randomUUID().toString());
			order.setOrderDate(LocalDate.now());
			order.setProduct(cart.getProduct());
			order.setPrice(cart.getProduct().getDiscountPrice());
			order.setQuantity(cart.getQuantity());
			order.setUser(cart.getUser());
			order.setStatus(OrderStatus.IN_PROGRESS.getName());
			order.setPaymentType(orRequestDto.getPaymentType());
			
			OrderAddress address=new OrderAddress();
			address.setFirstName(orRequestDto.getFirstName());
			address.setLastName(orRequestDto.getLastName());
			address.setEmail(orRequestDto.getEmail());
			address.setMobileNumber(orRequestDto.getMobileNumber());
			address.setAddress(orRequestDto.getAddress());
			address.setCity(orRequestDto.getCity());
			address.setState(orRequestDto.getState());
			address.setPincode(orRequestDto.getPincode());
			
			order.setOrderAddress(address);
			
			ProductOrder save = orderRepo.save(order);
			commonUtil.sendEmailForProductOrder(save, "Success");
		}
	}

	@Override
	public List<ProductOrder> getAllOrderedProducts(long userId) {
		List<ProductOrder> orders= orderRepo.findByUserUserId(userId);
		return orders;
	}

	@Override
	public ProductOrder updateOrderStatus(long id, String status) {
		ProductOrder productOrder = orderRepo.findById(id).get();
		if(!ObjectUtils.isEmpty(productOrder)) {
			productOrder.setStatus(status);
			return orderRepo.save(productOrder);
		}
		return null;
	}

	@Override
	public List<ProductOrder> getAllOrders() {
		return orderRepo.findAll();
	}

	@Override
	public ProductOrder searchOrder(String orderId) {
		return orderRepo.findByOrderId(orderId.trim());
	}

}
