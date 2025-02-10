package com.shoping.book_my_product.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.shoping.book_my_product.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 com.shoping.book_my_product.entity.UserDetails user = userRepo.findByEmail(username);
		if(ObjectUtils.isEmpty(user)) {
			throw new UsernameNotFoundException("User not found");
		}
		return new CustomUser(user);
	}

}
