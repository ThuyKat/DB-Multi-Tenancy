package multi_tenant.db.navigation.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import multi_tenant.db.navigation.Entity.Tenant.Permission;
import multi_tenant.db.navigation.Entity.Tenant.User;
import multi_tenant.db.navigation.Service.UserService;

@RestController
@RequestMapping("api/tenant")
public class UserController {
	@Autowired
	private UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@GetMapping("/user")
	public ResponseEntity<Object> getUser(@RequestHeader("shop-name") String shopName, @RequestParam String email ){
		System.out.println("Shop name: " + shopName);		
		User user = userService.getUserByEmailWithPermission(email);	
		
		//hibernate proxy, not fully loaded
		List<String> permissionList = user.getRole().getPermissions()
									.stream()
									.map(Permission::getName)
									.toList();
		
		logger.info("Username: {}", user.getFirstName());
		
		return new ResponseEntity<>(
				Map.of(
						"first-name", user.getFirstName(),
						"role", user.getRole().getName(),
						"permission", permissionList
						),
				HttpStatus.OK);
	}
	
}
