//package com.shoping.book_my_product.entity;
//
//import java.util.Date;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.ManyToOne;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//@Entity
//public class ProductOrder {
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private long id;
//	private String orderId;
//	private Date OrderDate;
//	@ManyToOne
//	private Product product;
//	private Double price;
//	private int quantity;
//	@ManyToOne
//	private UserDetails user;
//	private String status;
//	private String paymentType;
//}
