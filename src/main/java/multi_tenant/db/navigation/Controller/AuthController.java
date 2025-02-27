package multi_tenant.db.navigation.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import multi_tenant.db.navigation.DTO.UserResponse;
import multi_tenant.db.navigation.Entity.Tenant.User;
import multi_tenant.db.navigation.JWT.JwtTokenProvider;
import multi_tenant.db.navigation.Service.UserService;

@RestController
@RequestMapping("api/")
public class AuthController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestParam String email, @RequestParam String password) {	
		// from user input either username or email
		User user = userService.getUserByEmail(email);
		logger.debug("Login request receive for username/email: {}", email);
		if (user == null) {
			logger.warn("Loggin failed: User not found for username/email: {}",email);
			return new ResponseEntity<>(Map.of("error", "Invalid Username or Email."), HttpStatus.BAD_REQUEST);
		}
	
		if (!password.equals(user.getPassword())) {
			logger.warn("Login failed: Incorrect password for user ID: {}", user.getId());
			return new ResponseEntity<>(Map.of("error", "Incorrect password."), HttpStatus.BAD_REQUEST);
		}
		logger.info("User logged in successfully: {}", user.getFirstName());
		//generate Tokens, store username and role in token
		String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail(), List.of(user.getRole().getName()));
		String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail(), List.of(user.getRole().getName()));
		logger.debug("Generate tokens for user Id: {}", user.getId());
		Map<String, Object> response = new HashMap<>();
		response.put("message", "Login successfully.");
		response.put("accessToken", accessToken);
		response.put("refreshToken", refreshToken);
		response.put("user",
				new UserResponse(user.getId(), user.getFirstName(), user.getEmail(), user.getRole().getName()));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
