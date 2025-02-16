package com.shoping.book_my_product.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.shoping.book_my_product.entity.Category;
import com.shoping.book_my_product.entity.Product;
import com.shoping.book_my_product.entity.ProductOrder;
import com.shoping.book_my_product.entity.UserDetails;
import com.shoping.book_my_product.service.CartService;
import com.shoping.book_my_product.service.CategoryService;
import com.shoping.book_my_product.service.ProductOrderService;
import com.shoping.book_my_product.service.ProductService;
import com.shoping.book_my_product.service.UserService;
import com.shoping.book_my_product.util.CommonUtil;
import com.shoping.book_my_product.util.OrderStatus;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService prService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartService cartSer;
	
	@Autowired
	private ProductOrderService orderSer;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/")
	public String index() {
		return "admin/index";
	}
	@GetMapping("/addProduct")
	public String addProduct(Model model) {
		List<Category> list = categoryService.getAllCategory();
		model.addAttribute("categories", list);
		return "admin/addProduct";
	}
	@GetMapping("/category")
	public String category(Model model,@RequestParam(name  = "pageNo",defaultValue="0") Integer pageNo,
    		@RequestParam(name  = "pageSize",defaultValue="3") Integer pageSize) {
		//model.addAttribute("categories", categoryService.getAllCategory());
		Page<Category> page = categoryService.getAllCategoriesPagination(pageNo, pageSize);
		List<Category> categories = page.getContent();
        model.addAttribute("categories", categories);
        model.addAttribute("pageNo", page.getNumber());
        model.addAttribute("totalElements", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("isFirst", page.isFirst());
        model.addAttribute("isLast", page.isLast());
        model.addAttribute("pageSize", pageSize);
		return "admin/category";
	}
	
	@PostMapping("/saveCategory")
	public String saveCategory(@ModelAttribute Category category,@RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
		String imageName=file!=null ? file.getOriginalFilename():"default.jpg";
		category.setImageName(imageName);
		Boolean existCategory = categoryService.existCategory(category.getName());
		if(existCategory) {
			session.setAttribute("errorMsg", "Category name already exists");
		}else {
			Category saveCategory = categoryService.saveCategory(category);
			if(ObjectUtils.isEmpty(saveCategory)) {
				session.setAttribute("errorMsg", "! server error");
			}else {
				File saveFile=	new ClassPathResource("static/images").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"category_imgs"+File.separator+file.getOriginalFilename());
				System.out.println(path);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				session.setAttribute("succMsg", "saved successfully");
			}
		}
		return "redirect:/admin/category";
	}
	@GetMapping("/delete/{id}")
	public String deleteCategory(@PathVariable("id") long id,HttpSession session) {
		Boolean category = categoryService.deleteCategory(id);
		if(category) 
			session.setAttribute("succMsg", "Category deleted");
		else
			session.setAttribute("errorMsg", "Something wrong !");
		return "redirect:/admin/category";
	}
	@GetMapping("/edit/{id}")
	public String editCategory(@PathVariable("id") long id,Model model) {
		model.addAttribute("category", categoryService.getCategory(id));
		return "admin/edit_category";
	}
	@PostMapping("/updateCategory")
	public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,HttpSession session) throws IOException {
		Category category2 = categoryService.getCategory(category.getId());
		String newImage=file.isEmpty() ?category2.getImageName():file.getOriginalFilename();
		if(!ObjectUtils.isEmpty(category2)) {
			if(!file.isEmpty()) {
				File saveFile=	new ClassPathResource("static/images").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"category_imgs"+File.separator+file.getOriginalFilename());
				System.out.println(path);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
			category2.setName(category.getName());
			category2.setIsActive(category.getIsActive());
			category2.setImageName(newImage);
		}
		Category category3 = categoryService.saveCategory(category2);
		if(!ObjectUtils.isEmpty(category3)) {
			session.setAttribute("succMsg", "Category Updated");
		}else {
			session.setAttribute("errorMsg", "error occured");
		}
		//return "redirect:/admin/edit/"+category.getId();
		return "redirect:/admin/category";
	}

	
	//Product department
	@PostMapping("/saveProduct")
	public String saveProduct(@ModelAttribute Product product,@RequestParam("file")MultipartFile img,HttpSession session) throws IOException {
		String imgName=img.isEmpty()?"default.jpg":img.getOriginalFilename();
		product.setImage(imgName);
		product.setDiscount(0);
		product.setDiscountPrice(product.getPrice());
		Product saveProduct = prService.saveProduct(product);
		if(!ObjectUtils.isEmpty(saveProduct)) {
			File saveFile=	new ClassPathResource("static/images").getFile();
			Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"product_imgs"+File.separator+img.getOriginalFilename());
			System.out.println(path);
			Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			session.setAttribute("succMsg", "Product saved successfully");
		}else{
			session.setAttribute("errorMsg", "server error");
		}
		return "redirect:/admin/viewProduct";
	}
	@GetMapping("/viewProduct")
	public String viewProduct(Model model,@RequestParam(name  = "pageNo",defaultValue="0") Integer pageNo,
    		@RequestParam(name  = "pageSize",defaultValue="4") Integer pageSize) {
		//model.addAttribute("products", prService.getAllProducts());
		Page<Product> page = prService.getAllProductsPagination(pageNo, pageSize);
    	List<Product> products = page.getContent();
        model.addAttribute("products", products);
        model.addAttribute("productSize", products.size());
        model.addAttribute("pageNo", page.getNumber());
        model.addAttribute("totalElements", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("isFirst", page.isFirst());
        model.addAttribute("isLast", page.isLast());
        model.addAttribute("pageSize", pageSize);
		return "admin/view_products";
	}
	@GetMapping("/deleteProduct/{id}")
	public String deleteProduct(@PathVariable("id") long id,HttpSession session) {
		Boolean product = prService.deleteProduct(id);
		if(product)
			session.setAttribute("succMsg", "Product Deleted");
		else
			session.setAttribute("errorMsg", "error Occured !");
		return "redirect:/admin/viewProduct";
	}
	@GetMapping("/editProduct/{id}")
	public String editProduct(@PathVariable("id") long id,Model model) {
		model.addAttribute("product", prService.getProduct(id));
//		model.addAttribute("categories", categoryService.getAllCategory());
		return "admin/edit_product";
	}
	@PostMapping("/updateProduct")
	public String updateProduct(@ModelAttribute Product product,@RequestParam("file") MultipartFile file, HttpSession session)  {
		if(product.getDiscount()<0 || product.getDiscount()>100)
			session.setAttribute("errorMsg", "Invalid Discount");
		else {
			Product updateProduct = prService.updateProduct(product, file);
			if(!ObjectUtils.isEmpty(updateProduct)) 
				session.setAttribute("succMsg", "product updated");
			else
				session.setAttribute("errorMsg", "server error!");
		}
		return "redirect:/admin/viewProduct";
	}
	
	/* Users department */
	
	@ModelAttribute
	public void getUserDetails(Principal principal,Model model) {
		if(!ObjectUtils.isEmpty(principal)) {
			String name = principal.getName();
			UserDetails userDetails = userService.getUserByEmail(name);
			model.addAttribute("user", userDetails);
			long countCart = cartSer.getCountCart(userDetails.getUserId());
			model.addAttribute("countCart", countCart);
		}
		List<Category> categories = categoryService.getActiveCategories();
		model.addAttribute("categoryes", categories);
	}
	@GetMapping("/userslist")
	public String users(Model model,@RequestParam(name  = "pageNo",defaultValue="0") Integer pageNo,
    		@RequestParam(name  = "pageSize",defaultValue="3") Integer pageSize) {
		//model.addAttribute("users", userService.getAllUsers("ROLE_USER"));
		Page<UserDetails> page = userService.getAllUsersPagination(pageNo, pageSize);
    	List<UserDetails> users = page.getContent();
        model.addAttribute("users", users);
       // model.addAttribute("usersCount", users.size());
        model.addAttribute("pageNo", page.getNumber());
        model.addAttribute("totalElements", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("isFirst", page.isFirst());
        model.addAttribute("isLast", page.isLast());
        model.addAttribute("pageSize", pageSize);
		return "admin/users_list";
	}
	@GetMapping("/updateStatus")
	public String updateUserAccountStatus(@RequestParam Boolean status,@RequestParam Integer id,HttpSession session) {
		Boolean status2 = userService.updateAccountStatus(id,status);
		if(status2) {
			session.setAttribute("succMsg", "Account updated");
		}else {
			session.setAttribute("errorMsg", "server error!");
		}
		return "redirect:/admin/userslist";
	}
	@GetMapping("/orders")
	public String getOrders(Model model,Principal principal,@RequestParam(name  = "pageNo",defaultValue="0") Integer pageNo,
    		@RequestParam(name  = "pageSize",defaultValue="3") Integer pageSize) {
//		List<ProductOrder> orders = orderSer.getAllOrders();
//		model.addAttribute("orders", orders);
		Page<ProductOrder> page = orderSer.getAllOrderedProductsPagination(pageNo, pageSize);
    	List<ProductOrder> orders = page.getContent();
        model.addAttribute("orders", orders);
        model.addAttribute("orderSize", orders.size());
        model.addAttribute("pageNo", page.getNumber());
        model.addAttribute("totalElements", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("isFirst", page.isFirst());
        model.addAttribute("isLast", page.isLast());
        model.addAttribute("pageSize", pageSize);
		return "/admin/orders";
	}
	@PostMapping("/updateOrderState")
	public String updateOrderStatus(@RequestParam long id,@RequestParam Integer st,HttpSession session) {
		OrderStatus[] values = OrderStatus.values();
		String status=null;
		for (OrderStatus orderSta : values) {
			if(orderSta.getId().equals(st)) {
				status=orderSta.getName();
			}
		}
		ProductOrder orderStatus = orderSer.updateOrderStatus(id, status);
		try {
			commonUtil.sendEmailForProductOrder(orderStatus, status);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!ObjectUtils.isEmpty(orderStatus)) 
			session.setAttribute("succMsg", "Order status updated");
		else
			session.setAttribute("errorMsg", "Something went wrong");
		return "redirect:/admin/orders";
	}
	@GetMapping("/profile")
	public String profile() {
		return "/admin/profile";
	}
	@PostMapping("/updateProfile")
	public String updateProfile(@ModelAttribute UserDetails user,@RequestParam MultipartFile img,HttpSession session) {
		UserDetails details = userService.updateUserProfile(user, img);
		if(ObjectUtils.isEmpty(details)) {
			session.setAttribute("errorMsg", "Internal error");
		}else {
			session.setAttribute("succMsg", "Profile updated Successfully");
		}
		return "redirect:/admin/profile";
	}
	private UserDetails getLoggedInUserDetails(Principal principal) {
		String email = principal.getName();
		UserDetails userDetails = userService.getUserByEmail(email);
		return userDetails;
	}
	@PostMapping("/changePassword")
	public String changePassword(@RequestParam String newPass,@RequestParam String oldPass,Principal principal,HttpSession session) {
		UserDetails details = getLoggedInUserDetails(principal);
		boolean matches = passwordEncoder.matches(oldPass, details.getPassword());
		if(matches) {
			String encodeNew = passwordEncoder.encode(newPass);
			details.setPassword(encodeNew);
			UserDetails updateUser = userService.updateUserDetailsByPassword(details);
			if(!ObjectUtils.isEmpty(updateUser)) {
				session.setAttribute("succMs", "Password updated Successfully");
			}else {
				session.setAttribute("errorMs", "Somethig went wrong");
			}
			
		}else {
			session.setAttribute("errorMs", "Incorrect password");
		}
		return "redirect:/admin/profile";
	}
	
	@GetMapping("/searchOrder")
	public String searchOrder(@RequestParam String orderId,Model model,HttpSession session) {
		ProductOrder searchOrder = orderSer.searchOrder(orderId);
		if(ObjectUtils.isEmpty(searchOrder)) {
			if(orderId.length()>0) {
				session.setAttribute("errorMs", "Invalid Order Id");
			}else {
				model.addAttribute("orders", searchOrder);
				return "redirect:/admin/orders";
			}
		}else {
			model.addAttribute("orders", searchOrder);
		}
		return "/admin/orders";
	}
	@GetMapping("/searchProduct")
	public String searchProduct(@RequestParam String ch,Model model,HttpSession session) {
		List<Product> searchedProducts = prService.searchProduct(ch);
		if(ObjectUtils.isEmpty(searchedProducts)) {
			session.setAttribute("errorMs", "No Product Found");
		}else {
			model.addAttribute("products", searchedProducts);
		}
		return "admin/view_products";
	}
	@GetMapping("/searchCategory")
	public String searchCategory(@RequestParam String ch,Model model,HttpSession session) {
		List<Category> categories = categoryService.searchProduct(ch);
		if(ObjectUtils.isEmpty(categories)) {
			session.setAttribute("errorMs", "No category found");
		}else {
			model.addAttribute("categories", categories);
		}
		return "admin/category";
	}
	@GetMapping("/searchUser")
	public String searchUser(@RequestParam String ch,Model model,HttpSession session) {
		List<UserDetails> userByEmail = userService.searchUser(ch);
		if(ObjectUtils.isEmpty(userByEmail)) {
			session.setAttribute("errorMs", "No user found by email id");
		}else {
			model.addAttribute("users", userByEmail);
		}
		return "admin/users_list";
	}
}
