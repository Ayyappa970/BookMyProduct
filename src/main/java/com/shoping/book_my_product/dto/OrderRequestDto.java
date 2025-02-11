package com.shoping.book_my_product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class OrderRequestDto {
	private String firstName; 
	private String lastName;
	private String email;
	private String mobileNumber;
	private String address;
	private String city;
	private String state;
	private String pincode;
	private String paymentType;
}
