package com.Ecom.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.Ecom.Utils.AppConstant;
import com.Ecom.model.User;
import com.Ecom.repository.UserRepository;
import com.Ecom.service.UserService;

@Component
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User saveUserDetails(User user) {
		user.setRole("ROLE_USER");
		user.setIsEnabled(true);

		user.setAccountNonLocked(true);
		user.setFailedAtttempt(0);
		user.setLocalTime(null);

		String encodePassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodePassword);

		User user2 = userRepository.save(user);

		return user2;
	}

	@Override
	public User getUserByEmail(String email) {

		return userRepository.findByEmail(email);
	}

	@Override
	public List<User> getAllUsers(String role) {

		return userRepository.findByRole(role);
	}

	@Override
	public Boolean updateAcccountStatus(Integer id, Boolean status) {

		Optional<User> findByUser = userRepository.findById(id);

		if (findByUser.isPresent()) {
			User user = findByUser.get();
			user.setIsEnabled(status);
			userRepository.save(user);
			return true;
		}
		return false;
	}

	@Override
	public void increaseFailedAttempt(User user) {
		Integer attempt = user.getFailedAtttempt() + 1;
		user.setFailedAtttempt(attempt);
		userRepository.save(user);
	}

	@Override
	public void userAccountLock(User user) {
		user.setAccountNonLocked(false);
		user.setLocalTime(new Date());
		userRepository.save(user);

	}

	@Override
	public Boolean unlockAccountTimeExpired(User user) {

		long lockTime = user.getLocalTime().getTime();
		long unlockTime = lockTime + AppConstant.UNLOCK_DURATION_TIME;

		long currentTime = System.currentTimeMillis();

		if (unlockTime < currentTime) {
			user.setAccountNonLocked(true);
			user.setFailedAtttempt(0);
			user.setLocalTime(null);
			userRepository.save(user);

			return true;
		}

		return false;
	}

	@Override
	public void resetAttempts(int userId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateUserResetToken(String email, String resetToken) {
		User findByMail = userRepository.findByEmail(email);
		findByMail.setReset_token(resetToken);
		userRepository.save(findByMail);

	}

	@Override
	public User getUserByToken(String token) {
		return userRepository.findByResetToken(token);
	}

	@Override
	public User updateUser(User user) {

		return userRepository.save(user);
	}

	@Override
	public User updateUserProfile(User user, MultipartFile file) throws IOException {

		User existUser = userRepository.findById(user.getId()).get();

		if (!file.isEmpty()) {
			existUser.setImage(file.getOriginalFilename());
		}

		if (!ObjectUtils.isEmpty(existUser)) {
			existUser.setName(user.getName());
			existUser.setNumber(user.getNumber());
			existUser.setAddress(user.getAddress());
			existUser.setCity(user.getCity());
			existUser.setState(user.getState());
			existUser.setPincode(user.getPincode());
			existUser = userRepository.save(existUser);

		}

		if (!file.isEmpty()) {
			File file2 = new ClassPathResource("static/img").getFile();

			Path path = Paths.get(file2.getAbsolutePath() + File.separator + "profile_img" + File.separator
					+ file.getOriginalFilename());
			System.out.println(path);
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		}

		return existUser;
	}
	

	@Override
	public User saveAdminDetails(User user) {
		user.setRole("ROLE_ADMIN");
		user.setIsEnabled(true);

		user.setAccountNonLocked(true);
		user.setFailedAtttempt(0);
		user.setLocalTime(null);

		String encodePassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodePassword);

		User user2 = userRepository.save(user);

		return user2;
	}


}
