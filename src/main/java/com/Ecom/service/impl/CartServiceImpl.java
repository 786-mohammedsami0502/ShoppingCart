package com.Ecom.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.Ecom.model.Cart;
import com.Ecom.model.Product;
import com.Ecom.model.User;
import com.Ecom.repository.CartRepository;
import com.Ecom.repository.ProductRepository;
import com.Ecom.repository.UserRepository;
import com.Ecom.service.CartService;

@Service
public class CartServiceImpl implements CartService{
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProductRepository productRepository;

	@Override
	public Cart saveCart(Integer pid, Integer uid) {
		User user = userRepository.findById(uid).get();
//		System.out.println("USER "+user);
		Product product = productRepository.findById(pid).get();
//		System.out.println("PRODUCT"+product);
		Cart cartStatus = cartRepository.findByProductIdAndUserId(pid, uid);
		System.out.println("CART STATUS"+cartStatus);
		Cart cart = null;
		
		if(ObjectUtils.isEmpty(cartStatus)) {
			
			cart = new Cart();
			cart.setProduct(product);
			cart.setUser(user);
			cart.setQuantity(1);
			cart.setTotalPrice(product.getDiscountPrice());
		}else {
			cart = cartStatus;
			cart.setQuantity(cart.getQuantity()+1);
			cart.setTotalPrice(cart.getQuantity() * cart.getProduct().getDiscountPrice());
		}
		
		
		Cart saveCart = cartRepository.save(cart);
//		System.out.println("saveCart"+ saveCart);
		return saveCart;
	}

	@Override
	public List<Cart> getCartByUser(Integer userId) {
		List<Cart> carts = cartRepository.findByUserId(userId);
		System.out.println(" carts " + carts);
		Double totalOrderPrice = 0.0;
		
		List<Cart> updateCart = new ArrayList<>();
		System.out.println(updateCart);
		for(Cart c : carts) {
			Double totalPrice = (c.getProduct().getDiscountPrice() * c.getQuantity());
			c.setTotalPrice(totalPrice);
			
			totalOrderPrice += totalPrice;
			c.settotalOrderPrice(totalOrderPrice);
			updateCart.add(c);
		}
		 
//		carts.get(0).setTotalPrice(totalPrice);
		System.out.println(updateCart);
		return updateCart;
		
	}

	@Override
	public Integer getCountCart(Integer userId) {
		
		Integer countByUserId = cartRepository.countByUserId(userId);
		
		return countByUserId;
	}
	
	@Override
	public void updateQuantity(String sy, Integer cid) {
		
		Cart cart = cartRepository.findById(cid).get();
		Integer updateQuantity = 0;
		if(sy.equalsIgnoreCase("de")) {
			updateQuantity  = cart.getQuantity()-1; 
			if(updateQuantity <=0 ) {
				cartRepository.delete(cart);
			}else {
				cart.setQuantity(updateQuantity);
				cartRepository.save(cart);
			}
		}else {
			updateQuantity = cart.getQuantity()+1;
			cart.setQuantity(updateQuantity);
			cartRepository.save(cart);
		}
		
		
	}

	
}
