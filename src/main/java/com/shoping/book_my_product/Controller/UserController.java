package com.shoping.book_my_product.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shoping.book_my_product.dto.OrderRequestDto;
import com.shoping.book_my_product.entity.Cart;
import com.shoping.book_my_product.entity.Category;
import com.shoping.book_my_product.entity.ProductOrder;
import com.shoping.book_my_product.entity.UserDetails;
import com.shoping.book_my_product.service.CartService;
import com.shoping.book_my_product.service.CategoryService;
import com.shoping.book_my_product.service.ProductOrderService;
import com.shoping.book_my_product.service.UserService;
import com.shoping.book_my_product.util.OrderStatus;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryService catSer;
	
	@Autowired
	private CartService cartSer;
	
	@Autowired
	private ProductOrderService orderSer;
	
	@GetMapping("/")
	public String home() {
		return "/user/home";
	}
	
	@ModelAttribute
	public void getUserDetails(Principal principal,Model model) {
		if(!ObjectUtils.isEmpty(principal)) {
			String name = principal.getName();
			UserDetails userDetails = userService.getUserByEmail(name);
			model.addAttribute("user", userDetails);
			long countCart = cartSer.getCountCart(userDetails.getUserId());
			model.addAttribute("countCart", countCart);
		}
		List<Category> categories = catSer.getActiveCategories();
		model.addAttribute("categoryes", categories);
	}
	
	@GetMapping("/addCart")
	public String addToCart(@RequestParam Integer pid,@RequestParam Integer uid,HttpSession session) {
		Cart cart = cartSer.saveCart(pid, uid);
		if(ObjectUtils.isEmpty(cart)) {
			session.setAttribute("errorMsg", "Failed to Added Cart");
		}else {
			session.setAttribute("succMsg", "Successfully Added to Cart");
		}
		return "redirect:/product/"+pid;
	}
	
	@GetMapping("/carts")
	public String getMethodName(Principal principal,Model model) {
		UserDetails user= getLoggedInUserDetails(principal);
		List<Cart> list = cartSer.getCartByUser(user.getUserId());
		model.addAttribute("carts", list);
		if(list.size()>0) {
			Double totalOrderPrice = list.get(list.size()-1).getTotalOrderPrice();
			model.addAttribute("totalOrderPrice", totalOrderPrice);
		}
		return "/user/cart";
	}
	@GetMapping("/cartQuantityUpdate")
	public String updateCartQuantity(@RequestParam String sym,@RequestParam long cid) {
		cartSer.updateQuantity(sym, cid);
		return "redirect:/user/carts";
	}
	
	private UserDetails getLoggedInUserDetails(Principal principal) {
		String email = principal.getName();
		UserDetails userDetails = userService.getUserByEmail(email);
		return userDetails;
	}
	
	@GetMapping("/order")
	public String order(Principal principal,Model model) {
		UserDetails user= getLoggedInUserDetails(principal);
		List<Cart> list = cartSer.getCartByUser(user.getUserId());
		model.addAttribute("carts", list);
		if(list.size()>0) {
			Double orderPrice = list.get(list.size()-1).getTotalOrderPrice();
			model.addAttribute("orderPrice", orderPrice);
			Double totalOrderPrice = list.get(list.size()-1).getTotalOrderPrice()+250+100;
			model.addAttribute("totalOrderPrice", totalOrderPrice);
		}
		return "/user/order";
	}
	
	
	@PostMapping("/saveOrder")
	public String saveOrder(@ModelAttribute OrderRequestDto request,Principal principal) {
		//System.out.println(orderDto);
		orderSer.saveOrder(getLoggedInUserDetails(principal).getUserId(), request);
		return "redirect:/user/successPage";
	}
	@GetMapping("/successPage")
	public String successPage() {
		return "/user/order_success";
	}
	@GetMapping("/myOrders")
	public String myOrders(Model model,Principal principal) {
		List<ProductOrder> orders = orderSer.getAllOrderedProducts(getLoggedInUserDetails(principal).getUserId());
		model.addAttribute("orders", orders);
		return "/user/my_orders";
	}
	@GetMapping("/updateOrderStatus")
	public String updateOrderStatus(@RequestParam long id,@RequestParam Integer st,HttpSession session) {
		OrderStatus[] values = OrderStatus.values();
		String status=null;
		for (OrderStatus orderSta : values) {
			if(orderSta.getId().equals(st)) {
				status=orderSta.getName();
			}
		}
		Boolean orderStatus = orderSer.updateOrderStatus(id, status);
		if(orderStatus) 
			session.setAttribute("succMsg", "Order status updated");
		else
			session.setAttribute("errorMsg", "Something went wrong");
		return "redirect:/user/myOrders";
	}
}
