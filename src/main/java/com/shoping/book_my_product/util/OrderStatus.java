package com.shoping.book_my_product.util;

public enum OrderStatus {
	IN_PROGRESS(1,"In Progress"),
	ORDER_RECEIVED(2,"Received"),
	Product_Packed(3,"Packed"),
	OUT_FOR_DELIVERY(4,"Out for Delivery"),
	DELIVERED(5,"Delivered"),
	CANCELLED(6,"Cancelled"),
	SUCCESS(7,"Successful");
	private Integer id;
	private String name;
	private OrderStatus(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
