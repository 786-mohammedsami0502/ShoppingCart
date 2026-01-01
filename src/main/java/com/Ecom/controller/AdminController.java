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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
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
import com.Ecom.Utils.OrderStatus;
import com.Ecom.model.Category;
import com.Ecom.model.Product;
import com.Ecom.model.ProductOrder;
import com.Ecom.model.User;
import com.Ecom.repository.CategoryRepository;
import com.Ecom.repository.ProductOrderRepository;
import com.Ecom.repository.ProductRepository;
import com.Ecom.service.CartService;
import com.Ecom.service.CategoryService;
import com.Ecom.service.OrderService;
import com.Ecom.service.ProductService;
import com.Ecom.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("admin")
public class AdminController {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private CommonUtil commonUtil;

	@ModelAttribute
	public void getUserDetails(Principal principal, Model model) {
		if (principal != null) {
			String name = principal.getName();
			User userByEmail = userService.getUserByEmail(name);
			model.addAttribute("user", userByEmail);
//			Integer countCart = cartService.getCountCart(userByEmail.getId());
//			model.addAttribute("countCart", countCart);
		}
		List<Category> activeCategory = categoryService.getAllActiveCategory();
		model.addAttribute("activeCategory", activeCategory);
	}

	@GetMapping("/admin")
	public String admin() {
		return "admin/admin";
	}

	@GetMapping("/category")
	public String category(Model model, @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "2") Integer pageSize) {
//		m.addAttribute("categorys", categoryService.getAllCategory());
		Page<Category> page = categoryService.getAllCategoryPagination(pageNo, pageSize);
		List<Category> categorys = page.getContent();
		model.addAttribute("categorys", categorys);
		model.addAttribute("categorySize", categorys.size());

		model.addAttribute("pageNo", page.getNumber());
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("totalElements", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("isFirst", page.isFirst());
		model.addAttribute("isLast", page.isLast());
		return "admin/category";
	}

	@PostMapping("/saveCategory")
	public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
			@RequestParam("isActive") Boolean isActive, HttpSession httpSession) throws Exception {

//		@SuppressWarnings("null")
//		String imgName = (file!=null || file.isEmpty()) ? file.getOriginalFilename() : "default.png";
//		category.setImageName(imgName);

		if (file.isEmpty()) {
			category.setImageName("default.png");
			category.setActive(isActive);
			categoryRepository.save(category);
			return "redirect:/admin/category";
		} else {
			category.setImageName(file.getOriginalFilename());
		}

		category.setActive(isActive);

		Boolean existCategory = categoryService.existsCategory(category.getCateName());

		if (existCategory) {
			httpSession.setAttribute("errorMsg", " Category is already exist ");
		} else {

			Category saveCategory = categoryRepository.save(category);
			// saveCategory == null
			if (ObjectUtils.isEmpty(saveCategory)) {
				category.setImageName("default.png");
				httpSession.setAttribute("errorMsg", " Internal Server Error : Category can't empty ");
			} else {

				File f = new ClassPathResource("static/img").getFile();
				Path path = Paths
						.get(f.getAbsolutePath() + File.separator + "category_img" + file.getOriginalFilename());
				System.out.println("PATH " + path);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				category.setImageName(file.getOriginalFilename());

				httpSession.setAttribute("successMsg", " Saved SuccessFully!!!!!!! ");
			}
		}

		System.out.println(category.getCateName());
		System.out.println(category.getImageName());
		System.out.println(isActive);

		categoryRepository.save(category);

		return "redirect:/admin/category";
	}

	@GetMapping("/deletePC/{id}")
	public String deleteParticularCategory(@PathVariable int id, HttpSession session) {
		Boolean deleteCategory = categoryService.deleteCategory(id);

		if (deleteCategory) {
			session.setAttribute("successMsg", "Category Deleted Success Fully !!!!!");
		} else {
			session.setAttribute("errorMss", "Something Went Wrong");
		}

		return "redirect:/admin/category";
	}

	@GetMapping("/loadEditCategory/{id}")
	public String editCategory(@PathVariable int id, Model m) {

		Category categoryById = categoryService.getCategoryById(id);

		m.addAttribute("categoryById", categoryById);

		return "admin/edit_category";
	}

	@PostMapping("/updateCategory")
	public String updateParticularCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
			HttpSession session, @RequestParam("isActive") Boolean isActive) throws IOException {

		Category oldCategory = categoryService.getCategoryById(category.getId());

		if (!ObjectUtils.isEmpty(oldCategory)) {
			oldCategory.setCateName(category.getCateName());
			oldCategory.setActive(isActive);

			String fileName = file.isEmpty() ? oldCategory.getImageName() : file.getOriginalFilename();
			oldCategory.setImageName(fileName);
		}

		Category updateCategory = categoryService.saveCategory(oldCategory);

		if (!ObjectUtils.isEmpty(updateCategory)) {

			if (!file.isEmpty()) {
				File f = new ClassPathResource("/static/img").getFile();
				Path path = Paths
						.get(f.getAbsolutePath() + File.separator + "category_img" + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}

			session.setAttribute("successMsg", "Category Update Success");
		} else {
			session.setAttribute("errorMsg", "Something Went Wrong");
		}

		return "redirect:/admin/loadEditCategory/" + category.getId();
	}

	@GetMapping("/load_add_product")
	public String loadAddProduct(Model m) {

		List<Category> allCategory = categoryService.getAllCategory();
		m.addAttribute("allCategory", allCategory);

		return "admin/load_add_product";
	}

	@PostMapping("/saveProduct")
	public String saveProduct(@ModelAttribute Product product, HttpSession session,
			@RequestParam("file") MultipartFile file) throws IOException {

//		String fileName = (file.isEmpty()) ? "default.png" : file.getOriginalFilename();
//		product.setProdImage(fileName);

		if (file.isEmpty()) {

			product.setProdImage("default.img");
			productService.saveProduct(product);
			session.setAttribute("successMsg", "Product Save Successfully");
			return "redirect:/admin/load_add_product";
		} else {

			product.setProdImage(file.getOriginalFilename());
		}

		product.setDiscount(0);
		product.setDiscountPrice(product.getPrice());
		Product product2 = productService.saveProduct(product);

		if (!ObjectUtils.isEmpty(product2)) {

			File filePath = new ClassPathResource("/static/img").getFile();

			Path path = Paths.get(filePath.getAbsolutePath() + File.separator + "product_img" + File.separator
					+ file.getOriginalFilename());
			System.out.println(path);
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			session.setAttribute("successMsg", "Product Save Successfully");
		} else {
			session.setAttribute("errorMsg", "Something went wrong");
		}

		return "redirect:/admin/load_add_product";
	}

	@GetMapping("/view_product")
	public String viewProduct(@RequestParam(defaultValue = "") String ch, Model model,
			@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "2") Integer pageSize) {

		/*
		 * Code before implementing pagination List<Product> products = null; if(ch !=
		 * null && ch.length() > 0) { products= productService.searchProduct(ch); }else
		 * { products = productService.getAllProducts(); }
		 */

		Page<Product> page = null;
		if (ch != null && ch.length() > 0) {
			page = productService.searchProductPagination(pageNo, pageSize, ch);
		} else {
			page = productService.getAllProductsPagination(pageNo, pageSize);
		}

		model.addAttribute("products", page.getContent());
		model.addAttribute("pageNo", page.getNumber());
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("totalElements", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("isFirst", page.isFirst());
		model.addAttribute("isLast", page.isLast());

		return "admin/view_product";
	}

	@GetMapping("/deleteProduct/{id}")
	public String deleteProduct(@PathVariable int id, HttpSession session) {

		productService.deleteProduct(id);
		session.setAttribute("successMsg", "Product Deleted Successfully!!!!");

		return "redirect:/admin/view_product";
	}

	@GetMapping("/edit_product/{id}")
	public String editProduct(@PathVariable int id, Model m) {
		m.addAttribute("title", "Edit-Product");
		m.addAttribute("product", productService.getProductById(id));

		return "admin/edit_product";
	}

	@PostMapping("/updateProduct")
	public String updateProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile file,
			HttpSession session) throws IOException {

		Product currentProduct = productService.getProductById(product.getId());

//		String imageName = file.isEmpty() ? currentProduct.getProdImage() : file.getOriginalFilename();

		if (file.isEmpty()) {
			currentProduct.setProdImage(currentProduct.getProdImage());
		} else {
			currentProduct.setProdImage(file.getOriginalFilename());
		}

		currentProduct.setDescription(product.getDescription());
		currentProduct.setPrice(product.getPrice());
		currentProduct.setStock(product.getStock());
		currentProduct.setTitle(product.getTitle());
		currentProduct.setCategory(product.getCategory());

		currentProduct.setDiscount(product.getDiscount());

		double afterDiscount = product.getPrice() - ((product.getPrice() * product.getDiscount()) / 100);
		currentProduct.setDiscountPrice(afterDiscount);

		currentProduct.setIsActive(product.getIsActive());
		Product updateProduct = productRepository.save(currentProduct);

		if (!ObjectUtils.isEmpty(updateProduct)) {
			if (!file.isEmpty()) {
				File file2 = new ClassPathResource("/static/img").getFile();

				Path path = Paths.get(file2.getAbsolutePath() + File.separator + "/product_img" + File.separator
						+ file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
			session.setAttribute("successMsg", "Product Update SuccessFully!!!!!");
		} else {
			session.setAttribute("errorMsg", "Something Went Wrong.........");
		}

		return "redirect:/admin/edit_product/" + product.getId();
	}

	@GetMapping("/users")
	public String getAllUsers(Model m) {

		List<User> users = userService.getAllUsers("ROLE_USER");

		m.addAttribute("users", users);

		return "admin/users";
	}

	@GetMapping("/updateStatus")
	public String updateUserAccountStatus(@RequestParam Boolean status, @RequestParam Integer id, HttpSession session) {

		Boolean b = userService.updateAcccountStatus(id, status);

		if (b) {
			session.setAttribute("successMsg", "Account Updated");
		} else {
			session.setAttribute("errorMsg", "Someting Wrong");
		}

		return "redirect:/admin/users";
	}

	@GetMapping("/orders")
	public String getAllOrders(Model model, @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "2") Integer pageSize) {

		/*
		 * Code before implementing pagination List<ProductOrder> orders =
		 * orderService.getAllOrders(); model.addAttribute("orders", orders);
		 */

		Page<ProductOrder> page = orderService.getAllOrdersPagination(pageNo, pageSize);

		model.addAttribute("srch", false);

		model.addAttribute("orders", page.getContent());
		model.addAttribute("pageNo", page.getNumber());
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("totalElements", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("isFirst", page.isFirst());
		model.addAttribute("isLast", page.isLast());

		return "/admin/orders";
	}

	@PostMapping("/orders")
	public String updateOrderStatus(@RequestParam Integer id, @RequestParam Integer st, HttpSession session)
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

		ProductOrder updateOrder = orderService.updateOrderStatus(id, status);

		commonUtil.sendMailForProductOrder(updateOrder, status);

		if (!ObjectUtils.isEmpty(updateOrder)) {
			session.setAttribute("successMsg", "Status updated");
		} else {
			session.setAttribute("errorMsg", "Status not updated........");
		}

		return "redirect:/admin/orders";
	}

	@GetMapping("/search-order")
	public String searchProduct(@RequestParam String orderId, Model model, HttpSession session,
			@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "3") Integer pageSize) {

		if (orderId != null && orderId.length() > 0) {
			ProductOrder order = orderService.getOrdersByOrderId(orderId.trim());
			if (ObjectUtils.isEmpty(order)) {
				session.setAttribute("errorMsg", " Incorrect orderId ");
				model.addAttribute("order", null);
			} else {
				System.out.println(" order - " + order);
				model.addAttribute("order", order);
			}

			model.addAttribute("srch", true);
		} else {
			Page<ProductOrder> page = orderService.getAllOrdersPagination(pageNo, pageSize);
			session.setAttribute("errorMsg", " Enter Order Id for searching	 ");
			model.addAttribute("orders", page.getContent());
			model.addAttribute("srch", false);

			model.addAttribute("pageNo", page.getNumber());
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("totalElements", page.getTotalElements());
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("isFirst", page.isFirst());
			model.addAttribute("isLast", page.isLast());

		}
		return "/admin/orders";
	}

	@GetMapping("/show-admin")
	public String editAdmin(Principal principal, Model model) {

		User admin = userService.getUserByEmail(principal.getName());
		System.out.println(" ADMIN " + admin);
		model.addAttribute("admin", admin);
		return "admin/edit_admin";
	}

	@PostMapping("/edit-admin")
	public String saveUser(@ModelAttribute User user, @RequestParam("file") MultipartFile file, HttpSession session)
			throws IOException {

		if (file.isEmpty()) {
			user.setImage("default.png");
			userService.saveAdminDetails(user);
		} else {
			user.setImage(file.getOriginalFilename());
		}

		User saveUser = userService.saveAdminDetails(user);

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
		return "admin/edit_admin";
	}

}
