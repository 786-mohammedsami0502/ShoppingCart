package com.Ecom.service.impl;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.Ecom.service.CommonService;

import jakarta.servlet.http.HttpSession;

@Component
public class CommonServiceImpl implements CommonService {

	@Override
	public void removeMessageFromSession() {
		
		try {
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest().getSession();
		session.removeAttribute("successMsg");
		session.removeAttribute("errorMsg");
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
