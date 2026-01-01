package com.Ecom.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.data.domain.Page;

import com.Ecom.model.OrderRequest;
import com.Ecom.model.ProductOrder;

import jakarta.mail.MessagingException;

public interface OrderService {

	public void saveOrder(Integer userId, OrderRequest orderRequest) throws UnsupportedEncodingException, MessagingException;
	
	public List<ProductOrder> getOrdersByUser(Integer userId);
	
	public ProductOrder updateOrderStatus(Integer id, String status);
	
	public List<ProductOrder> getAllOrders();
	
	public ProductOrder getOrdersByOrderId(String orderId);
	
	public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize);
	
}
