package com.shoping.book_my_product.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shoping.book_my_product.entity.UserDetails;
import com.shoping.book_my_product.repository.UserRepository;
import com.shoping.book_my_product.service.UserService;
import com.shoping.book_my_product.util.AppConstant;

import io.micrometer.common.util.StringUtils;
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails addUser(UserDetails user) {
		user.setRole("ROLE_USER");
		user.setIsEnable(true);
		user.setAccountNonLocked(true);
		user.setFailedAttempt(0);
		String encode = passwordEncoder.encode(user.getPassword());
		user.setEmail(user.getEmail().toLowerCase());
		user.setPassword(encode);
		return userRepo.save(user);
	}

	@Override
	public UserDetails getUserByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	@Override
	public List<UserDetails> getAllUsers(String role) {
		return userRepo.findByRole(role);
	}

	@Override
	public Boolean updateAccountStatus(Integer id, Boolean status) {
		UserDetails details = userRepo.findById(id).get();
		if(!ObjectUtils.isEmpty(details)) {
			details.setIsEnable(status);
			userRepo.save(details);
			return true;
		}
		return false;
	}

	@Override
	public void increaseFailedAttempt(UserDetails user) {
		int attempt=user.getFailedAttempt()+1;
		user.setFailedAttempt(attempt);
		userRepo.save(user);
	}

	@Override
	public void userAccountLock(UserDetails user) {
		user.setAccountNonLocked(false);
		user.setLockTime(new Date());
		userRepo.save(user);
	}

	@Override
	public boolean unLockAccountTimeExpired(UserDetails user) {
		long lockTime = user.getLockTime().getTime();
		long unlockTime=lockTime+AppConstant.UNLOCK_DURATION_TIME;
		long currentTime = System.currentTimeMillis();
		if(unlockTime<currentTime) {
			user.setAccountNonLocked(true);
			user.setFailedAttempt(0);
			user.setLockTime(null);
			userRepo.save(user);
			return true;  
		}
		return false;
	}

	@Override
	public void resetAttempt(Integer userId) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void updateUserResetToken(String email, String restToken) {
		UserDetails byEmail = userRepo.findByEmail(email);
		byEmail.setResetToken(restToken);
		userRepo.save(byEmail);
	}

	@Override
	public UserDetails getUserByToken(String token) {
		return userRepo.findByResetToken(token);
	} 
	
	@Override
	public UserDetails updateUserDetailsByPassword(UserDetails user) {
		return userRepo.save(user);
	}

	@Override
	public UserDetails updateUserProfile(UserDetails user,MultipartFile img) {
		UserDetails details = userRepo.findById(user.getUserId()).get();
		if(!img.isEmpty()) {
			details.setProfileImage(img.getOriginalFilename());
		}
		if(!ObjectUtils.isEmpty(details)) {
			details.setUserName(user.getUserName());
			details.setMobileNumber(user.getMobileNumber());
			details.setAddress(user.getAddress());
			details.setCity(user.getCity());
			details.setState(user.getState());
			details.setPincode(user.getPincode());
			details=userRepo.save(details);
			
		}
		try {
			if(!img.isEmpty()) {
				File saveFile=	new ClassPathResource("static/images").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"user_imgs"+File.separator+img.getOriginalFilename());
				System.out.println(path);
				Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return details;
	}

	@Override
	public List<UserDetails> searchUser(String ch,String role) {
		return userRepo.findByEmailContainingIgnoreCaseAndRole(ch, role);
	}
//	@Override
//	public List<UserDetails> searchAdmin(String ch,String role) {
//		return userRepo.findByEmailContainingIgnoreCaseAndRole(ch, role);
//	}

	@Override
	public Page<UserDetails> getAllUsersPagination(Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		return userRepo.findAll(pageable);
	}
	@Override
	public UserDetails saveAdmin(UserDetails user) {
		user.setRole("ROLE_ADMIN");
		user.setIsEnable(true);
		user.setAccountNonLocked(true);
		user.setFailedAttempt(0);
		String encode = passwordEncoder.encode(user.getPassword());
		user.setPassword(encode);
		user.setEmail(user.getEmail().toLowerCase());
		return userRepo.save(user);
	}

	@Override
	public Page<UserDetails> getAllUsersByRole(Integer pageNo, Integer pageSize, String role) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<UserDetails> users=null;
		if(StringUtils.isEmpty(role)) {
			users=userRepo.findAll(pageable);
		}else {
			users=userRepo.findByRole(pageable, role);
		}
		return users;
	}

	@Override
	public Boolean existsEmail(String email) {
		return userRepo.existsByEmail(email);
	}

	
}
