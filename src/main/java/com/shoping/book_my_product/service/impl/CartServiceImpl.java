package com.shoping.book_my_product.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.shoping.book_my_product.entity.Cart;
import com.shoping.book_my_product.entity.Product;
import com.shoping.book_my_product.entity.UserDetails;
import com.shoping.book_my_product.repository.CartRepository;
import com.shoping.book_my_product.repository.ProductRepository;
import com.shoping.book_my_product.repository.UserRepository;
import com.shoping.book_my_product.service.CartService;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Override
	public Cart saveCart(Integer productId, Integer UserId) {
		UserDetails user= userRepo.findById(UserId).get();
		Product product = productRepo.findById(productId).get();
		Cart cartStatus = cartRepo.findByProductIdAndUserUserId(productId, UserId);
		Cart cart=cartStatus;
		if(ObjectUtils.isEmpty(cart)) {
			cart=new Cart();
			cart.setProduct(product);
			cart.setUser(user);
			cart.setQuantity(1);
			cart.setTotalPrice(1*product.getDiscountPrice());
		}else {
			cart=cartStatus;
			cart.setQuantity(cart.getQuantity()+1);
			cart.setTotalPrice(cart.getQuantity()*cart.getProduct().getDiscountPrice());
		}
		return cartRepo.save(cart);
	}

	@Override
	public List<Cart> getCartByUser(Integer userId) {
		List<Cart> carts = cartRepo.findByUserUserId(userId);
		Double totalOrderPrice=0.0;
		List<Cart> updateCarts=new ArrayList<>();
		for(Cart c:carts) {
			Double totalPrice=(c.getProduct().getDiscountPrice()*c.getQuantity());
			c.setTotalPrice(Math.round(totalPrice * 100.0) / 100.0);
			totalOrderPrice=totalOrderPrice+totalPrice;
			c.setTotalOrderPrice(totalOrderPrice);
			updateCarts.add(c);
		}
		return updateCarts;
	}

	@Override
	public long getCountCart(Integer userId) {
		long count = cartRepo.countByUserUserId(userId);
		return count;
	}

	@Override
	public void updateQuantity(String sym, Integer cid) {
		Cart cart = cartRepo.findById(cid).get();
		int updateQuantity;
		if(sym.equalsIgnoreCase("dec")) {
			updateQuantity=cart.getQuantity()-1;
			if(updateQuantity<=0) {
				cartRepo.deleteById(cid);
			}else {
				cart.setQuantity(updateQuantity);
				cartRepo.save(cart);
			}
		}else {
			updateQuantity=cart.getQuantity()+1;
			cart.setQuantity(updateQuantity);
			cartRepo.save(cart);
		}
		
		
	}

}
