package com.Ecom.service;

import java.util.List;

import com.Ecom.model.Cart;

public interface CartService {
	
	public Cart saveCart(Integer pid, Integer uid);

	public List<Cart> getCartByUser(Integer userId);
	
	public Integer getCountCart(Integer userId);

	public void updateQuantity(String sy, Integer cid);
}
