package com.Ecom.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.Ecom.Utils.CommonUtil;
import com.Ecom.model.Category;
import com.Ecom.model.Product;
import com.Ecom.model.User;
import com.Ecom.service.CartService;
import com.Ecom.service.CategoryService;
import com.Ecom.service.ProductService;
import com.Ecom.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private UserService userService;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private CartService cartService;

	@ModelAttribute
	public void getUserDetails(Principal principal, Model model) {
		if (principal != null) {
			String name = principal.getName();
			User userByEmail = userService.getUserByEmail(name);
			model.addAttribute("user", userByEmail);
			Integer countCart = cartService.getCountCart(userByEmail.getId());
			model.addAttribute("countCart", countCart);
		}

		List<Category> activeCategory = categoryService.getAllActiveCategory();
		model.addAttribute("activeCategory", activeCategory);
	}

	@GetMapping("/index")
	public String home(Model model) {
		
		List<Category> allActiveCategory = categoryService.getAllActiveCategory().stream().limit(6).toList();
		
		List<Product> allActiveProducts = productService.getAllActiveProduct("").stream()	
//				.sorted((p1,p2) ->  p2.getId().compareTo(p1.getId())
						.limit(8).toList();

		Integer.signum(10) ; 
		
		System.out.println(" allActiveCategory " + allActiveCategory);
		System.out.println(" Image " + allActiveCategory.get(0).getImageName());
		
		model.addAttribute("category", allActiveCategory);
		model.addAttribute("products", allActiveProducts);

		
		return "index";
	}

//	@GetMapping("/index")
//	public String index() {
//		return "index";
//	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/signup")
	public String register() {
		return "signup";
	}

	@GetMapping("/product")
	public String product(Model model, @RequestParam(value = "category", defaultValue = "") String category,
			@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "3") Integer pageSize) {

		List<Category> categories = categoryService.getAllActiveCategory();
		model.addAttribute("categories", categories);
		model.addAttribute("paramValue", category);

		Page<Product> page = productService.getAllActiveProductPagination(pageNo, pageSize, category);
		List<Product> products = page.getContent();
		model.addAttribute("products", products);
		model.addAttribute("productSize", products.size());

		model.addAttribute("pageNo", page.getNumber());
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("totalElements", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("isFirst", page.isFirst());
		model.addAttribute("isLast", page.isLast());

		return "product";
	}

	@GetMapping("/view_product/{id}")
	public String viewProduct(@PathVariable("id") int id, Model model) {

		Product productById = productService.getProductById(id);

		model.addAttribute("productById", productById);

		return "view_product";
	}

	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute User user, @RequestParam("file") MultipartFile file, HttpSession session)
			throws IOException {

		if (file.isEmpty()) {
			user.setImage("default.png");
			userService.saveUserDetails(user);
		} else {
			user.setImage(file.getOriginalFilename());
		}

		User saveUser = userService.saveUserDetails(user);

		if (!ObjectUtils.isEmpty(saveUser)) {

			if (!file.isEmpty()) {

				File file2 = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(file2.getAbsolutePath() + File.separator + "user_img" + File.separator
						+ file.getOriginalFilename());
				System.out.println("PATH = " + path);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				user.setImage(file.getOriginalFilename());
				session.setAttribute("successMsg", "User Save SussessFully !!!!!!!!!!!!");
			} else {
				session.setAttribute("errorMsg", "User Not Save !!!!!!!!!!!!");
			}
		}
		return "redirect:/signup";
	}

	// This methods for forgot password

	@GetMapping("/forgot-password")
	public String showForgotpasword() {
		return "forgot_password";
	}

	@PostMapping("/forgot-password")
	public String processForgotpasword(@RequestParam String email, HttpSession session, HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException {

		User userByEmail = userService.getUserByEmail(email);

		if (!ObjectUtils.isEmpty(userByEmail)) {

			String resetToken = UUID.randomUUID().toString();
			userService.updateUserResetToken(email, resetToken);

			// Generate URL :
			// http://localhost:8080/reset-password?token=qwertyuiopsdfghjklmnbvcxz

			String url = CommonUtil.generateUrl(request) + "/reset-password?token=" + resetToken;
			System.out.println(url);

			Boolean sentMail = commonUtil.sentMail(url, email);

			if (sentMail) {
				session.setAttribute("successMsg", "Link sent to your email for resetting password");
			} else {
				session.setAttribute("errorMsg", "Something went wrong!!!!!!!!");
			}
		} else {
			session.setAttribute("errorMsg", "Invalid email");
		}

		return "redirect:/forgot-password";
	}

	@GetMapping("/reset-password")
	public String showResetPassword(@RequestParam String token, Model model) {

		User userByToken = userService.getUserByToken(token);

		if (ObjectUtils.isEmpty(userByToken)) {
			model.addAttribute("msg", "Your link is Invalid or Expired!!!!!!!!!");
			return "message";
		}
		model.addAttribute("token", token);
		return "reset_password";
	}

	@PostMapping("/reset-password")
	public String pesetPassword(@RequestParam String token, @RequestParam String password, HttpSession session,
			Model model) {

		User userByToken = userService.getUserByToken(token);

		if (ObjectUtils.isEmpty(userByToken)) {

			model.addAttribute("msg", "Your link is Invalid or Expired!!!!!!!!!");
			return "message";
		} else {
			userByToken.setPassword(passwordEncoder.encode(password));
			userByToken.setReset_token(null);
			userService.updateUser(userByToken);
//			session.setAttribute("msg", "Password change successfully :) ");
			model.addAttribute("msg", " Password change successfully :) ");
			return "message";
		}
	}

	@GetMapping("/search")
	public String searchProduct(@RequestParam String ch, Model model) {
		List<Product> searchProducts = productService.searchProduct(ch);
		model.addAttribute("products", searchProducts);
		List<Category> categories = categoryService.getAllActiveCategory();
		model.addAttribute("categories", categories);

		return "product";
	}

}
