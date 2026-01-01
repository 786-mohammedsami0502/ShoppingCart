package com.Ecom.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.Ecom.model.User;

public interface UserService {
	
	public User saveUserDetails(User user);
	
	public User getUserByEmail(String email);

	public List<User> getAllUsers(String role);

	//This method for enabling user status
	public Boolean updateAcccountStatus(Integer id, Boolean status);

	/*
	 *  This methods for adding Wrong Password Limit
	 */
	
	// This method for Increase Failed Attempts count 
	public void increaseFailedAttempt(User user);
	
	// This method for locking accounts
	public void userAccountLock(User user);
	
	// This method for unlocking time accounts
	public Boolean unlockAccountTimeExpired(User user);
	
	// This method for reseting attempts
	public void resetAttempts(int userId);

	public void updateUserResetToken(String email, String resetToken);

	public User getUserByToken(String token);

	public User updateUser(User user);
	
	public User updateUserProfile(User user, MultipartFile file) throws IOException;

	public User saveAdminDetails(User user);
	
	
}
