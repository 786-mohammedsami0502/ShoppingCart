package com.Ecom.Utils;

public enum OrderStatus {

	
	IN_PROGRESS(1,"In Progress"),
	ORDER_RECEIVED(2,"Order Received"),
	PRODUCT_PACKED(3,"Product Packed"),
	OUT_FOR_DELIVERY(4,"Out For Delivery"),
	DELIVERED(5,"Delivered"),
	CANCEL(6,"Cancelled"),
	SUCCESS(7,"Success");
	
	private Integer id;
	
	private String name;

	private OrderStatus(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
