package com.Ecom.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.Ecom.Utils.CommonUtil;
import com.Ecom.Utils.OrderStatus;
import com.Ecom.model.Cart;
import com.Ecom.model.Category;
import com.Ecom.model.OrderRequest;
import com.Ecom.model.ProductOrder;
import com.Ecom.model.User;
import com.Ecom.service.CartService;
import com.Ecom.service.CategoryService;
import com.Ecom.service.OrderService;
import com.Ecom.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@ModelAttribute
	public void getUserDetails(Principal principal, Model model) {
		if (principal != null) {
			String name = principal.getName();
			User userByEmail = userService.getUserByEmail(name);
			System.out.println(" userByEmail " + userByEmail);
			model.addAttribute("user", userByEmail);

			Integer countCart = cartService.getCountCart(userByEmail.getId());
			model.addAttribute("countCart", countCart);
		}
		List<Category> activeCategory = categoryService.getAllActiveCategory();
		model.addAttribute("activeCategory", activeCategory);
	}

	@GetMapping("/")
	public String home() {
		return "user/home";
	}

	@GetMapping("/addCart")
	public String addToCart(@RequestParam int pid, @RequestParam int uid, HttpSession session) {

		Cart saveCart = cartService.saveCart(pid, uid);

		if (ObjectUtils.isEmpty(saveCart)) {
			session.setAttribute("errorMsg", " Product add to cart failed ");
		} else {
			session.setAttribute("successMsg", " Product add to cart successfully ");
		}

		return "redirect:/view_product/" + pid;
	}

	@GetMapping("/cart")
	public String loadCartPage(Principal p, Model model) {

		User user = getLoggedInUserDetails(p);

		List<Cart> carts = cartService.getCartByUser(user.getId());
		model.addAttribute("carts", carts);
		if (carts.size() > 0) {
			Double totalOrderPrice = carts.get(carts.size() - 1).gettotalOrderPrice();

			model.addAttribute("totalOrderPrice", totalOrderPrice);
		}
		return "user/cart";
	}

	private User getLoggedInUserDetails(Principal p) {
		String email = p.getName();
		User user = userService.getUserByEmail(email);
		return user;
	}

	@GetMapping("cartQuantityUpdate")
	public String updateCartQuantity(@RequestParam String sy, @RequestParam Integer cid) {

		cartService.updateQuantity(sy, cid);

		return "redirect:/user/cart";
	}

	@GetMapping("/order")
	public String orderPage(Principal p, Model model) {

		User user = getLoggedInUserDetails(p);

		List<Cart> carts = cartService.getCartByUser(user.getId());
		model.addAttribute("carts", carts);
		if (carts.size() > 0) {
			Double totalOrderPrice = carts.get(carts.size() - 1).gettotalOrderPrice();
			model.addAttribute("totalOrderPrice", totalOrderPrice);

			Double deliveryCharge = 1000.0;

			Double tax = (totalOrderPrice * 18) / 100;
			model.addAttribute("tax", tax);

			Double calc = 0.0;
			if (totalOrderPrice < 100000) {
				calc = totalOrderPrice + tax + deliveryCharge;
				model.addAttribute("deliveryCharge", deliveryCharge);
				model.addAttribute("calc", calc);
			} else {
				calc = totalOrderPrice + tax;
				model.addAttribute("deliveryCharge", 0);
				model.addAttribute("calc", calc);
			}
		}

		return "/user/order";
	}

	@PostMapping("/saveOrder")
	public String saveOrder(@ModelAttribute OrderRequest request, Principal principal)
			throws UnsupportedEncodingException, MessagingException {
//		System.out.println(request.toString());
		User loggedInUser = getLoggedInUserDetails(principal);
		orderService.saveOrder(loggedInUser.getId(), request);
		return "redirect:/user/order_success";
	}

	@GetMapping("/order_success")
	public String loadSuccess() {
		return "/user/order_success";
	}

	@GetMapping("/my_orders")
	public String myOrders(Principal principal, Model model) {

		User loggedInUser = getLoggedInUserDetails(principal);
		List<ProductOrder> orders = orderService.getOrdersByUser(loggedInUser.getId());
		model.addAttribute("orders", orders);
		return "/user/my_orders";

	}

	@GetMapping("/update-status")
	public String updateOrderStatus(@RequestParam Integer id, @RequestParam String st, HttpSession session)
			throws UnsupportedEncodingException, MessagingException {

		OrderStatus[] values = OrderStatus.values();
		String status = null;

		for (OrderStatus orderStatus : values) {
			System.out.println(" orderStatus " + orderStatus);
			System.out.println(" orderStatus.getId() " + orderStatus.getId());
			if (orderStatus.getId().equals(st)) {
				status = orderStatus.getName();
			}
		}

		ProductOrder updateOrderStatus = orderService.updateOrderStatus(id, status);

		commonUtil.sendMailForProductOrder(updateOrderStatus, status);

		if (!ObjectUtils.isEmpty(updateOrderStatus)) {
			session.setAttribute("successMsg", "Status updated");
		} else {
			session.setAttribute("errorMsg", "Status not updated........");
		}

		return "redirect:/user/my_orders";
	}

	@GetMapping("/profile")
	public String profile() {
		return "/user/profile";
	}

	@PostMapping("/update-profile")
	public String updateProfile(@ModelAttribute User user, @RequestParam MultipartFile file, HttpSession session)
			throws IOException {

		User updateUserProfile = userService.updateUserProfile(user, file);

		if (!ObjectUtils.isEmpty(updateUserProfile)) {
			session.setAttribute("successMsg", " Profile updated successfully");
		} else {
		}

		return "redirect:/user/profile";
	}

	@PostMapping("/change-password")
	public String changePassword(@RequestParam String newPassword, @RequestParam String currentPassword,
			Principal principal, HttpSession session) {
		User loggedInUser = getLoggedInUserDetails(principal);

		boolean matches = passwordEncoder.matches(currentPassword, loggedInUser.getPassword());

		if (matches) {
			String encodePassword = passwordEncoder.encode(newPassword);
			loggedInUser.setPassword(encodePassword);
			User updateUser = userService.updateUser(loggedInUser);
			
			if(!ObjectUtils.isEmpty(updateUser)) {
			session.setAttribute("successMsg", " Password update successfully");
			}else {
				session.setAttribute("errorMsg", " Password not updated!!! ");
			}
		} else {
			session.setAttribute("errorMsg", " Current password incorrect");
		}

		return "redirect:/user/profile";
	}
}
