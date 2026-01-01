package com.Ecom.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.Ecom.model.Product;

public interface ProductService {
	// This code for Admin Controller
	public Product saveProduct(Product product);
	
	public List<Product> getAllProducts();
	
	public void deleteProduct(int id);
	
	public Product getProductById(Integer id);
	// This code for Admin Controller
	
	// This code for Home Controller
	public List<Product> getAllActiveProduct(String category);
	
	public List<Product> searchProduct(String ch);
	
	public Page<Product> getAllActiveProductPagination(Integer pageNo, Integer pageSize, String category);
	
	public Page<Product> searchProductPagination(Integer pageNo, Integer pageSize, String ch);
	
	public Page<Product> getAllProductsPagination(Integer pageNo, Integer pageSize);
}
