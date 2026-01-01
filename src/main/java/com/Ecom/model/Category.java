package com.Ecom.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Category {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private int id;
	private String cateName;
	private String imageName;
	
	private Boolean isActive;
	
	
	
	/**
	 * @return the isActive
	 */
	public Boolean isActive() {
		return isActive;
	}
	/**
	 * @param isActive the isActive to set
	 */
	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the cateName
	 */
	public String getCateName() {
		return cateName;
	}
	/**
	 * @param cateName the cateName to set
	 */
	public void setCateName(String cateName) {
		this.cateName = cateName;
	}
	/**
	 * @return the imageName
	 */
	public String getImageName() {
		return imageName;
	}
	/**
	 * @param imageName the imageName to set
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	public Category(int id, String cateName, String imageName, boolean isActive) {
		super();
		this.id = id;
		this.cateName = cateName;
		this.imageName = imageName;
		this.isActive=isActive;
	}
	public Category() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Category [id=" + id + ", cateName=" + cateName + ", imageName=" + imageName + "isActive" + isActive + "]";
	}
	
	
	
	
}
