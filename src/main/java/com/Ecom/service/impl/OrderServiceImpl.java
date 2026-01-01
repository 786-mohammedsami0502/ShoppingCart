package com.Ecom.service.impl;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.Ecom.Utils.CommonUtil;
import com.Ecom.Utils.OrderStatus;
import com.Ecom.model.Cart;
import com.Ecom.model.OrderAddress;
import com.Ecom.model.OrderRequest;
import com.Ecom.model.ProductOrder;
import com.Ecom.repository.CartRepository;
import com.Ecom.repository.ProductOrderRepository;
import com.Ecom.service.OrderService;

import jakarta.mail.MessagingException;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private ProductOrderRepository productOrderRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CommonUtil commonUtil;

	@Override
	public void saveOrder(Integer userId, OrderRequest orderRequest) throws UnsupportedEncodingException, MessagingException {

		List<Cart> carts = cartRepository.findByUserId(userId);

		for (Cart cart : carts) {

			ProductOrder order = new ProductOrder();

			order.setOrderId(UUID.randomUUID().toString());
			order.setOrderDate(LocalDate.now());

			order.setProduct(cart.getProduct());
			order.setPrice(cart.getProduct().getDiscountPrice());

			order.setQuantity(cart.getQuantity());
			order.setUser(cart.getUser());

			order.setOrderStatus(OrderStatus.IN_PROGRESS.name());
			order.setPaymentType(orderRequest.getPaymentType());

			OrderAddress orderAddress = new OrderAddress();
			orderAddress.setName(orderRequest.getName());
			orderAddress.setNumber(orderRequest.getNumber());
			orderAddress.setEmail(orderRequest.getEmail());
			orderAddress.setAddress(orderRequest.getAddress());
			orderAddress.setCity(orderRequest.getCity());
			orderAddress.setState(orderRequest.getState());
			orderAddress.setPincode(orderRequest.getPincode());

			order.setOrderAddress(orderAddress);

			ProductOrder savedOrder = productOrderRepository.save(order);

			commonUtil.sendMailForProductOrder(savedOrder, "success");
		}

	}

	@Override
	public List<ProductOrder> getOrdersByUser(Integer userId) {
		List<ProductOrder> orders = productOrderRepository.findByUserId(userId);
		return orders;
	}

	@Override
	public ProductOrder updateOrderStatus(Integer id, String status) {

		Optional<ProductOrder> findById = productOrderRepository.findById(id);

		if (findById.isPresent()) {
			ProductOrder productOrder = findById.get();
			productOrder.setOrderStatus(status);
			ProductOrder updateOrder = productOrderRepository.save(productOrder);
			return updateOrder;
		}

		return null;

	}

	@Override
	public List<ProductOrder> getAllOrders() {
		return productOrderRepository.findAll();
	}
	
	@Override
	public ProductOrder getOrdersByOrderId(String orderId) {
		return productOrderRepository.findByOrderId(orderId);

	}
	
	@Override
	public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize) {
		
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		
		return productOrderRepository.findAll(pageable);
		
	}

}
