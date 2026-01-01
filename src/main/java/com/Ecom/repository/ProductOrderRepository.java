package com.Ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecom.model.ProductOrder;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer> {

	public List<ProductOrder>  findByUserId(Integer userId);

	public ProductOrder findByOrderId(String orderId);

}
