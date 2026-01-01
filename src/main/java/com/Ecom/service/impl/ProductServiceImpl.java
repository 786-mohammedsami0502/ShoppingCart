package com.Ecom.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.Ecom.model.Category;
import com.Ecom.model.Product;
import com.Ecom.repository.CategoryRepository;
import com.Ecom.repository.ProductRepository;
import com.Ecom.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	// <!-- This code for

	@Autowired
	private ProductRepository productRepository;

	@Override
	public Product saveProduct(Product product) {
//		Product product2 = productRepository.save(product);
		return productRepository.save(product);
	}

	@Override
	public List<Product> getAllProducts() {
//		List<Product> allProduct = productRepository.findAll();
		return productRepository.findAll();
	}

	@Override
	public void deleteProduct(int id) {
		productRepository.deleteById(id);
	}

	@Override
	public Product getProductById(Integer id) {
		Product product = productRepository.findById(id).orElse(null);
		return product;
	}

	// Admin Controller !-->

	// <!-- This code for

	@Override
	public List<Product> getAllActiveProduct(String category) {

		List<Product> isActiveTrue = null;

		if (ObjectUtils.isEmpty(category)) {
			isActiveTrue = productRepository.findByIsActiveTrue();
		} else {
			isActiveTrue = productRepository.findByCategory(category);
		}

		return isActiveTrue;
	}

	// Home Controller !-->

	@Override
	public List<Product> searchProduct(String ch) {
		return productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch);

	}

	@Override
	public Page<Product> getAllActiveProductPagination(Integer pageNo, Integer pageSize, String category) {
		
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Product> pageProduct = null;

		if (ObjectUtils.isEmpty(category)) {
			pageProduct = productRepository.findByIsActiveTrue(pageable);
		} else {
			pageProduct = productRepository.findByCategory(pageable,category);
		}

		return pageProduct;
	}
	
	@Override
	public Page<Product> searchProductPagination(Integer pageNo, Integer pageSize, String ch) {
		Pageable pagable = PageRequest.of(pageNo, pageSize);
		return productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch, pagable );
		
	}
	@Override
	public Page<Product> getAllProductsPagination(Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		return productRepository.findAll(pageable);
		
	}

}
