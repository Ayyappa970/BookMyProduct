package com.shoping.book_my_product.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;
	private String userName;
	private String mobileNumber;
	private String email;
	private String address;
	private String city;
	private String state;
	private int pincode;
	private String password;
	private String profileImage;
	private String role;
	private Boolean isEnable;
	private Boolean accountNonLocked;
	private Integer failedAttempt;
	private String lockTime;
	private String resetToken;
}
