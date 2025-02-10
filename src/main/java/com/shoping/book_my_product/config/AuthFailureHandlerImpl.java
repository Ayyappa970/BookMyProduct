package com.shoping.book_my_product.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.shoping.book_my_product.entity.UserDetails;
import com.shoping.book_my_product.repository.UserRepository;
import com.shoping.book_my_product.service.UserService;
import com.shoping.book_my_product.util.AppConstant;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler{
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserService userService;
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		String email = request.getParameter("username");
		UserDetails user = userRepo.findByEmail(email);
		if(user.getIsEnable()) {
			if(user.getAccountNonLocked()) {
				if(user.getFailedAttempt()<AppConstant.ATTEMPT_TIMES) {
					userService.increaseFailedAttempt(user);
				}else {
					userService.userAccountLock(user);
					exception=new LockedException("Your account is locked !!  attemped 3 times");
				}
			}else {
				if(userService.unLockAccountTimeExpired(user)) {
					exception=new LockedException("Your account is unlocked !! Please try ato login");
				}else {
					exception=new LockedException("Your account is locked !! Try again after some time");
				}
			}
		}else {
			exception=new LockedException("Your account is inactive");
		}
		super.setDefaultFailureUrl("/signin?error");
		super.onAuthenticationFailure(request, response, exception);
	}

}
