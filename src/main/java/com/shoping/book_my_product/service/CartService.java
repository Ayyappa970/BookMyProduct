package com.shoping.book_my_product.service;

import java.util.List;

import com.shoping.book_my_product.entity.Cart;

public interface CartService {
	public Cart saveCart(long productId,long UserId);
	
	public List<Cart> getCartByUser(long userId);
	
	public long getCountCart(long userId);

	public void updateQuantity(String sym, long cid);
	
}
