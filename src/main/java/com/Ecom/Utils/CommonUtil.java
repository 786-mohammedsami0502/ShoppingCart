package com.Ecom.Utils;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.Ecom.model.ProductOrder;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CommonUtil {

	@Autowired
	private JavaMailSender javaMailSender;
	
	public Boolean sentMail(String url, String recipientEmail) throws UnsupportedEncodingException, MessagingException {
		
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom("samimatheen2002@gmail.com", "Shopping_Cart");
		helper.setTo(recipientEmail);
		
		String content= "<p>Hello, </p>" + "<p> You have requested to reset you password.</p>" + 
		"<p> Click the link below to change your password </p>" + "<p><a href=\"" +url + "\"> Change my password</a></p>";
		
		helper.setSubject("Password Reset");
		helper.setText(content,true);
		javaMailSender.send(message);
		return true;
	}

	public static String generateUrl(HttpServletRequest request) {
		
		String siteUrl = request.getRequestURL().toString();
		System.out.println(" siteUrl "+siteUrl);
		System.out.println(" siteUrl.replace(request.getServletPath(), \"\") "+siteUrl.replace(request.getServletPath(), ""));
		return siteUrl.replace(request.getServletPath(), "") ;
		
	}
	
	String msg=null;
	
	
	
	public Boolean sendMailForProductOrder(ProductOrder productOrder, String status) throws MessagingException, UnsupportedEncodingException {
		
		msg = "<p> Mr.[[name]], </p> <p>Thanks... You order <b>[[orderedStatus]]</b>. </p>"+
				"<b><p> Product Details </p> </b>"+
				"<p>Name : [[productName]] </p>"+
				"<p>Category : [[category]] </p>"+
				"<p>Quantity : [[quantity]] </p>"+
				"<p>Price : [[price]] </p>"+
				"<p>Payment Type : [[paymentType]] </p>";
		
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom("samimatheen2002@gmail.com", "Shopping_Cart");
		helper.setTo(productOrder.getOrderAddress().getEmail());
		
		msg=msg.replace("[[name]]", productOrder.getOrderAddress().getName());
		msg=msg.replace("[[orderedStatus]]", status);
		msg=msg.replace("[[productName]]", productOrder.getProduct().getTitle());
		msg=msg.replace("[[category]]", productOrder.getProduct().getCategory());
		msg=msg.replace("[[quantity]]", productOrder.getQuantity().toString());
		msg=msg.replace("[[price]]", productOrder.getPrice().toString());
		msg=msg.replace("[[paymentType]]", productOrder.getPaymentType());
		
		helper.setSubject("Product Ordered Status");
		helper.setText(msg,true);
		javaMailSender.send(message);
		return true;
		
	}
}
