package com.Ecom.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

@Entity
public class Cart {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Integer id;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private Product product;
	
	private Integer quantity;
	
	@Transient
	private Double totalPrice;
	
	@Transient
	private Double totalOrderPrice;
	

	public Cart() {
		// TODO Auto-generated constructor stub
	}


	public Cart(Integer id, User user, Product product, Integer quantity, Double totalPrice, Double totalOrderPrice) {
		super();
		this.id = id;
		this.user = user;
		this.product = product;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.totalOrderPrice = totalOrderPrice;
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
	 * @return the totalPrice
	 */
	public Double getTotalPrice() {
		return totalPrice;
	}


	/**
	 * @param totalPrice the totalPrice to set
	 */
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}


	/**
	 * @return the totalOrderPrice
	 */
	public Double gettotalOrderPrice() {
		return totalOrderPrice;
	}


	/**
	 * @param totalOrderPrice the totalOrderPrice to set
	 */
	public void settotalOrderPrice(Double totalOrderPrice) {
		this.totalOrderPrice = totalOrderPrice;
	}


	@Override
	public String toString() {
		return "Cart [id=" + id + ", user=" + user + ", product=" + product + ", quantity=" + quantity + ", totalPrice="
				+ totalPrice + ", totalOrderPrice=" + totalOrderPrice + "]";
	}
	

	
}
