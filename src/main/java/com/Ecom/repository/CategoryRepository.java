package com.Ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecom.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

	public Boolean existsByCateName(String name);

	public List<Category> findByIsActiveTrue();

	
}
