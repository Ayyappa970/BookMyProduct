package com.shoping.book_my_product.service;

import java.util.List;

import com.shoping.book_my_product.entity.Cart;

public interface CartService {
	public Cart saveCart(Integer productId,Integer UserId);
	
	public List<Cart> getCartByUser(Integer userId);
	
	public long getCountCart(Integer userId);

	public void updateQuantity(String sym, Integer cid);
	
}
