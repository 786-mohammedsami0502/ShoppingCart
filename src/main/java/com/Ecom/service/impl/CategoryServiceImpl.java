package com.Ecom.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.Ecom.model.Category;
import com.Ecom.repository.CategoryRepository;
import com.Ecom.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Category saveCategory(Category category) {
		return categoryRepository.save(category);
	}

	@Override
	public Boolean existsCategory(String name) {

		return categoryRepository.existsByCateName(name);
	}

	@Override
	public List<Category> getAllCategory() {

		return categoryRepository.findAll();
	}

	@Override
	public Boolean deleteCategory(int id) {

		Category category = categoryRepository.findById(id).orElse(null);

		if (!ObjectUtils.isEmpty(category)) {
			categoryRepository.deleteById(id);
			return true;
		}

		return false;
	}

	@Override
	public Category getCategoryById(int id) {

		Category category = categoryRepository.findById(id).orElse(null);
		return category;
	}

	@Override
	public List<Category> getAllActiveCategory() {
		List<Category> categories = categoryRepository.findByIsActiveTrue();
		return categories;
	}

	@Override
	public Page<Category> getAllCategoryPagination(Integer pageNo, Integer pageSize) {

		Pageable pageable = PageRequest.of(pageNo, pageSize);
		return categoryRepository.findAll(pageable);

	}

}
