package com.Ecom.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String number;
	private String email;
	@Column(length=100)
	private String address;
	private String city;
	private String state;
	private String pincode;
	private String password;
	private String image;
	
	// This module for adding roles. When i added role module
	private String role;

	// This module for enabling user status. When i added user status 
	private Boolean isEnabled;

	// This module for adding Wrong Password Limit
	private Boolean accountNonLocked;
	
	private Integer failedAtttempt;
	
	private Date localTime;
	
	private String resetToken;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the pincode
	 */
	public String getPincode() {
		return pincode;
	}

	/**
	 * @param pincode the pincode to set
	 */
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @return the isEnabled
	 */
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	/**
	 * @param isEnabled the isEnabled to set
	 */
	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * @return the accountNonLocked
	 */
	public Boolean getAccountNonLocked() {
		return accountNonLocked;
	}

	/**
	 * @param accountNonLocked the accountNonLocked to set
	 */
	public void setAccountNonLocked(Boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	/**
	 * @return the failedAtttempt
	 */
	public Integer getFailedAtttempt() {
		return failedAtttempt;
	}

	/**
	 * @param failedAtttempt the failedAtttempt to set
	 */
	public void setFailedAtttempt(Integer failedAtttempt) {
		this.failedAtttempt = failedAtttempt;
	}

	/**
	 * @return the localTime
	 */
	public Date getLocalTime() {
		return localTime;
	}

	/**
	 * @param localTime the localTime to set
	 */
	public void setLocalTime(Date localTime) {
		this.localTime = localTime;
	}

	/**
	 * @return the reset_token
	 */
	public String getReset_token() {
		return resetToken;
	}

	/**
	 * @param reset_token the reset_token to set
	 */
	public void setReset_token(String reset_token) {
		this.resetToken = reset_token;
	}

	public User(Integer id, String name, String number, String email, String address, String city, String state,
			String pincode, String password, String image, String role, Boolean isEnabled, Boolean accountNonLocked,
			Integer failedAtttempt, Date localTime, String reset_token) {
		super();
		this.id = id;
		this.name = name;
		this.number = number;
		this.email = email;
		this.address = address;
		this.city = city;
		this.state = state;
		this.pincode = pincode;
		this.password = password;
		this.image = image;
		this.role = role;
		this.isEnabled = isEnabled;
		this.accountNonLocked = accountNonLocked;
		this.failedAtttempt = failedAtttempt;
		this.localTime = localTime;
		this.resetToken = reset_token;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", number=" + number + ", email=" + email + ", address=" + address
				+ ", city=" + city + ", state=" + state + ", pincode=" + pincode + ", password=" + password + ", image="
				+ image + ", role=" + role + ", isEnabled=" + isEnabled + ", accountNonLocked=" + accountNonLocked
				+ ", failedAtttempt=" + failedAtttempt + ", localTime=" + localTime + ", reset_token=" + resetToken
				+ "]";
	}

	public User() {
		// TODO Auto-generated constructor stub
	}

}