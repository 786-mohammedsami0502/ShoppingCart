package com.Ecom.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.Ecom.Utils.AppConstant;
import com.Ecom.model.User;
import com.Ecom.repository.UserRepository;
import com.Ecom.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		String email = request.getParameter("username");
		
		User userDetails = userRepository.findByEmail(email);
		if(userDetails != null) {
		if(userDetails.getIsEnabled()) {
			
			if(userDetails.getAccountNonLocked()) {
				
				if(userDetails.getFailedAtttempt() < AppConstant.ATTEMPT_TIME) {
					if(userDetails.getFailedAtttempt()!=null) {
						exception = new LockedException(" After 3 attempt you account will lock for 30-Sec :"
								+ " Current Attempt " + userDetails.getFailedAtttempt()+1 );
					}
					userService.increaseFailedAttempt(userDetails);
				}else {
					userService.userAccountLock(userDetails);
					exception = new LockedException(" Your account is locked!!! Failed 3 attempt");
				}
				
			}else {
				if(userService.unlockAccountTimeExpired(userDetails)) {
					exception = new LockedException(" Your account is unlocked!!! Please try again");
				} else {
					exception = new LockedException(" Your account is locked!!! Try after sometime");
				}					
			}
			
		}else {
			exception = new LockedException(" Your account is inactive");
		}
		}else {
			exception = new LockedException(" Invalid Email I's and password !!! User is not registered.....  ");
		}
		
		super.setDefaultFailureUrl("/login?error");
		super.onAuthenticationFailure(request, response, exception);
	}

	
}
