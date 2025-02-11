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
	public String category(Model model) {
		model.addAttribute("categories", categoryService.getAllCategory());
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
		return "redirect:/admin/edit/"+category.getId();
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
		return "redirect:/admin/addProduct";
	}
	@GetMapping("/viewProduct")
	public String viewProduct(Model model) {
		model.addAttribute("products", prService.getAllProducts());
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
		return "redirect:/admin/editProduct/"+product.getId();
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
	public String users(Model model) {
		model.addAttribute("users", userService.getAllUsers("ROLE_USER"));
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
	public String getOrders(Model model,Principal principal) {
		List<ProductOrder> orders = orderSer.getAllOrders();
		model.addAttribute("orders", orders);
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
		Boolean orderStatus = orderSer.updateOrderStatus(id, status);
		if(orderStatus) 
			session.setAttribute("succMsg", "Order status updated");
		else
			session.setAttribute("errorMsg", "Something went wrong");
		return "redirect:/admin/orders";
	}
}
