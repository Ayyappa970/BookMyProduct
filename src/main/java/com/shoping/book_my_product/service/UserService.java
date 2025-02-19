package com.shoping.book_my_product.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.shoping.book_my_product.entity.UserDetails;

public interface UserService {
	public UserDetails addUser(UserDetails user);
	public UserDetails getUserByEmail(String email);
	public List<UserDetails> getAllUsers(String role);
	public Boolean updateAccountStatus(Integer id, Boolean status);
	public void increaseFailedAttempt(UserDetails user);
	public void userAccountLock(UserDetails user);
	public boolean unLockAccountTimeExpired(UserDetails user);
	public void resetAttempt(Integer userId);
	public void updateUserResetToken(String email, String restToken);
	public UserDetails getUserByToken(String token);
	public UserDetails updateUserDetailsByPassword(UserDetails user);
	public UserDetails updateUserProfile(UserDetails user,MultipartFile img);
	public List<UserDetails> searchUser(String ch,String role);
	//public List<UserDetails> searchAdmin(String ch,String role);
	public Page<UserDetails> getAllUsersPagination(Integer pageNo, Integer pageSize);
	public UserDetails saveAdmin(UserDetails user);
	public Page<UserDetails> getAllUsersByRole(Integer pageNo, Integer pageSize, String role);
	public Boolean existsEmail(String email);
	
}
