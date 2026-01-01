package com.Ecom.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.Ecom.model.Category;


public interface CategoryService {
	
	public Category saveCategory(Category category);
	
	public Boolean existsCategory(String name);
	
	public List<Category> getAllCategory();

	public Boolean deleteCategory(int id);
	
	public Category getCategoryById(int id);
	
	
	// This code for Home Controller
	public List<Category> getAllActiveCategory();
	
	public Page<Category> getAllCategoryPagination(Integer pageNo, Integer pageSize );
}
