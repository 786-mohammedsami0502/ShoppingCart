package com.Ecom.model;

import java.time.LocalDate;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class ProductOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Integer id;
	
	private String orderId;
	
	private LocalDate orderDate;
	
	@ManyToOne
	private Product product;
	
	private Double price;
	
	private Integer quantity;
	
	@ManyToOne
	private User user;
	
	private String orderStatus;
	
	private String paymentType;
	
	@OneToOne(cascade = CascadeType.ALL)
	private OrderAddress orderAddress;
	
	public ProductOrder() {
		// TODO Auto-generated constructor stub
	}

	public ProductOrder(Integer id, String orderId, LocalDate orderDate, Product product, Double price,
			Integer quantity, User user, String orderStatus, String paymentType, OrderAddress orderAddress) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.orderDate = orderDate;
		this.product = product;
		this.price = price;
		this.quantity = quantity;
		this.user = user;
		this.orderStatus = orderStatus;
		this.paymentType = paymentType;
		this.orderAddress = orderAddress;
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
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the orderDate
	 */
	public LocalDate getOrderDate() {
		return orderDate;
	}

	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * @return the product
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

	/**
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(Double price) {
		this.price = price;
	}

	/**
	 * @return the quantity
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the orderStatus
	 */
	public String getOrderStatus() {
		return orderStatus;
	}

	/**
	 * @param orderStatus the orderStatus to set
	 */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	/**
	 * @return the paymentType
	 */
	public String getPaymentType() {
		return paymentType;
	}

	/**
	 * @param paymentType the paymentType to set
	 */
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	/**
	 * @return the orderAddress
	 */
	public OrderAddress getOrderAddress() {
		return orderAddress;
	}

	/**
	 * @param orderAddress the orderAddress to setx`x`
	 */
	public void setOrderAddress(OrderAddress orderAddress) {
		this.orderAddress = orderAddress;
	}

	@Override
	public String toString() {
		return "ProductOrder [id=" + id + ", orderId=" + orderId + ", orderDate=" + orderDate + ", product=" + product
				+ ", price=" + price + ", quantity=" + quantity + ", user=" + user + ", orderStatus=" + orderStatus
				+ ", paymentType=" + paymentType + ", orderAddress=" + orderAddress + "]";
	}

		
	
	
}
