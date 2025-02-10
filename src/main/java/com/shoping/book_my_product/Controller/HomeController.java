package com.shoping.book_my_product.Controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.shoping.book_my_product.entity.Category;
import com.shoping.book_my_product.entity.Product;
import com.shoping.book_my_product.entity.UserDetails;
import com.shoping.book_my_product.service.CartService;
import com.shoping.book_my_product.service.CategoryService;
import com.shoping.book_my_product.service.ProductService;
import com.shoping.book_my_product.service.UserService;
import com.shoping.book_my_product.util.CommonUtil;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	@Autowired
	private CategoryService catSer;
	
	@Autowired
	private ProductService proSer;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private CartService cartSer;
	
    @GetMapping("/")
    public String index(){
        return "index";
    }
    @GetMapping("/signin")
    public String login(){
        return "login";
    }
    @GetMapping("/register")
    public String register(){
        return "register";
    }
    @GetMapping("/products")
    public String products(Model model,@RequestParam(value = "category",defaultValue="") String category){
    	model.addAttribute("categories", catSer.getActiveCategories());
    	model.addAttribute("products", proSer.getAllActiveProducts(category));
    	model.addAttribute("parmValue", category);
        return "product";
    }
    @GetMapping("/product/{id}")
    public String product(@PathVariable("id") int id,Model model){
    	Product product = proSer.getProduct(id);
    	model.addAttribute("product", product);
        return "viewProduct";
    }
    

	
	@PostMapping("/saveUser")
	public String savUser(@ModelAttribute UserDetails user,@RequestParam("file") MultipartFile file,HttpSession session) throws IOException {
		String imageName=file.isEmpty()?"default.jpg":file.getOriginalFilename();
		user.setProfileImage(imageName);
		UserDetails userDetails = userService.addUser(user);
		if(!ObjectUtils.isEmpty(userDetails)) {
			if(!file.isEmpty()) {
				File saveFile=	new ClassPathResource("static/images").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"user_imgs"+File.separator+file.getOriginalFilename());
				System.out.println(path);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				session.setAttribute("succMsg", "user registration successfull");
			}
			session.setAttribute("errorMsg", "something wrong!");
		}else {
			session.setAttribute("errorMsg", "something went wrong!");
		}
		return "redirect:/register";
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
	
	@GetMapping("/forgotPassword")
	public String forgotPassword() {
		return "forgot_password";
	}
	
	@PostMapping("/forgotPassword")
	public String workOnforgotPassword(String email,HttpSession session,HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		UserDetails email2 = userService.getUserByEmail(email);
		if(ObjectUtils.isEmpty(email2)) {
			session.setAttribute("errorMsg", "Invalid email address");
		}else {
			String restToken = UUID.randomUUID().toString();
			userService.updateUserResetToken(email,restToken);
			
			//generate url :http://localhost:8080/resetPassword?token=gbvbrtygfdbv
			String url=CommonUtil.generateUrl(request)+"/resetPassword?token="+restToken;
			
			Boolean sendMail = commonUtil.sendMail(url,email);
			if(sendMail) {
				session.setAttribute("succMsg", "Reset link will sended on your email");
			}else {
				session.setAttribute("errorMsg", "error occured");
			}
		}
		return "redirect:/forgotPassword";
	}
	
	@GetMapping("/resetPassword")
	public String resetPassword(@RequestParam String token,Model model) {
		UserDetails userByToken = userService.getUserByToken(token);
		if(ObjectUtils.isEmpty(userByToken)) {
			model.addAttribute("message", "link invalid or expired !!");
			return "error";
		}
		model.addAttribute("token", token);
		return "reset_password";
	}
	
	@PostMapping("/resetPassword")
	public String resetPasswordUpdate(@RequestParam String token,@RequestParam String password,Model model,HttpSession session) {
		UserDetails userByToken = userService.getUserByToken(token);
		if(ObjectUtils.isEmpty(userByToken)) {
			model.addAttribute("message", "link invalid or expired !!");
			return "error";
		}else {
			userByToken.setPassword(passwordEncoder.encode(password));
			userByToken.setResetToken(null);
			userService.updateUserDetailsByPassword(userByToken);
			session.setAttribute("succMsg", "password updated successfully");
			model.addAttribute("message", "password updated successfully");
			return "error";
		}
	}
}
