//package multi_tenant.db.navigation.Controller;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import multi_tenant.db.navigation.DTO.UserResponse;
//import multi_tenant.db.navigation.Entity.Global.Developer;
//import multi_tenant.db.navigation.Entity.Global.Owner;
//import multi_tenant.db.navigation.Entity.Global.Tenant;
//import multi_tenant.db.navigation.Entity.Tenant.User;
//import multi_tenant.db.navigation.JWT.JwtTokenProvider;
//import multi_tenant.db.navigation.Service.DeveloperService;
//import multi_tenant.db.navigation.Service.OwnerService;
//import multi_tenant.db.navigation.Service.UserService;
//
////@RestController
////@RequestMapping("api/")
//public class ManualAuthController {
//	@Autowired
//	private UserService userService;
//	
//	@Autowired
//	private OwnerService ownerService;
//	
//	@Autowired
//	private DeveloperService developerService;
//	
//	@Autowired
//	private JwtTokenProvider jwtTokenProvider;
//	
//	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
//	
//	@PostMapping("/login")
//	public ResponseEntity<Object> login(
//			@RequestHeader(value = "shop-name", required = false) String shopName,
//			@RequestHeader(value = "global-user", required = false) String globalUser,
//			@RequestParam String email, @RequestParam String password) {	
//		
//		if(shopName == null) {
//			if(globalUser != null) {				
//					return authenticatedUser(email, password, globalUser);		
//			}else {
//				return new ResponseEntity<>(Map.of("error", "Invalid Login details."), HttpStatus.BAD_REQUEST);
//			}
//		}else {
//			// from user input either username or email
//			return authenticatedUser(email, password, "USER");
//		}		
//		
//	}
//	private ResponseEntity<Object> authenticatedUser(String email, String password, String userType) {
//		Object user = null;
//		logger.debug("Login request receive for username/email: {}", email);
//		switch (userType) {
//		case "OWNER":
//			user = ownerService.getOwnerByEmail(email);
//			break;
//		case "DEVELOPER":
//			user = developerService.getDeveloperByEmail(email);
//			break;
//		case "USER":
//			user = userService.getUserByEmail(email);
//			break;
//		default:
//			return new ResponseEntity<>(Map.of("error", "Invalid Login details."), HttpStatus.BAD_REQUEST);
//		}
//				
//		if (user == null) {
//			logger.warn("Loggin failed: User not found for username/email: {}",email);
//			return new ResponseEntity<>(Map.of("error", "Invalid Username or Email."), HttpStatus.BAD_REQUEST);
//		}
//		
//		String storedPassword = getPasswordFromUser(user);
//		Long storedId = getUserId(user);
//		String storedFirstName = getUserFirstName(user);
//		List<String> roles = getRoleFromUser(user);
//		if (!password.equals(storedPassword)) {
//			logger.warn("Login failed: Incorrect password for user ID: {}", storedId);
//			return new ResponseEntity<>(Map.of("error", "Incorrect password."), HttpStatus.BAD_REQUEST);
//		}
//		logger.info("User logged in successfully: {}", storedFirstName);
//		//generate Tokens, store username and role in token
//		String accessToken = jwtTokenProvider.generateAccessToken(email, roles);
//		String refreshToken = jwtTokenProvider.generateRefreshToken(email, roles);
//		
//		logger.debug("Generate tokens for user Id: {}", storedId);
//		Map<String, Object> response = new HashMap<>();
//		response.put("message", "Login successfully.");
//		response.put("accessToken", accessToken);
//		response.put("refreshToken", refreshToken);
//		response.put("user",
//				new UserResponse(storedId, storedFirstName, email, roles));
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}
//	
//	private String getPasswordFromUser(Object user) {
//		if(user instanceof Owner) return ((Owner) user).getPassword();
//		if(user instanceof Developer) return ((Developer) user).getPassword();
//		if(user instanceof User) return ((User) user).getPassword();
//		return "";
//	}
//	private Long getUserId(Object user) {
//	    if (user instanceof Owner) return ((Owner) user).getId();
//	    if (user instanceof Developer) return ((Developer) user).getId();
//	    if (user instanceof User) return ((User) user).getId();
//	    return 0L;
//	}
//	private List<String> getRoleFromUser(Object user) {
//	    if (user instanceof Owner) {	    	
//	    	return  Arrays.stream(Tenant.Role.values())
//	    			.map(role -> role.name())
//	    			.collect(Collectors.toList());}
//	    if (user instanceof Developer) return List.of(((Developer) user).getRole().toString());
//	    if (user instanceof User) return List.of(((User) user).getRole().getName());
//	    return Collections.emptyList();
//	}
//	
//	private String getUserFirstName(Object user) {
//	    if (user instanceof Owner) return ((Owner) user).getFirstName();
//	    if (user instanceof Developer) return ((Developer) user).getFirstName();
//	    if (user instanceof User) return ((User) user).getFirstName();
//	    return "";
//	}
//}
