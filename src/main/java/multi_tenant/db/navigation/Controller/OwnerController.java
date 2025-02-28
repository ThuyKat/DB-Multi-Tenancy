package multi_tenant.db.navigation.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import multi_tenant.db.navigation.DTO.TenantResponse;
import multi_tenant.db.navigation.DTO.UserResponse;
import multi_tenant.db.navigation.Entity.Global.Owner;
import multi_tenant.db.navigation.Entity.Global.Tenant;
import multi_tenant.db.navigation.Entity.Tenant.User;
import multi_tenant.db.navigation.Service.OwnerService;
import multi_tenant.db.navigation.Service.TenantService;
import multi_tenant.db.navigation.Service.UserService;

@RestController
@RequestMapping("/api")
public class OwnerController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private TenantService tenantService;
	
	@Autowired
	private OwnerService ownerService;
	
	@PreAuthorize("hasRole('OWNER')")
	@GetMapping("/owner/tenant-list")
	public ResponseEntity<Object> getTenantList(Principal principal){
		Owner owner = ownerService.getOwnerByEmail(principal.getName());
				
		List<Tenant> tenants = tenantService.getTenantsByOwnerId(owner.getId());
		List<TenantResponse> tenantResponse
						= tenants.stream()
						.map(tenant -> new TenantResponse(
							tenant.getId(),
							tenant.getName(),
							tenant.getDbName(),
							tenant.getStatus().toString()						
						))
						.collect(Collectors.toList());
		
		return new ResponseEntity<>(Map.of(
				"message", "Successfully get Tenant List", 
				"TenantList", tenantResponse), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('OWNER','ADMIN)")
	@GetMapping("/admin/user-list")
	public ResponseEntity<Object> getUserList(){
		List<User> users = userService.getAllUsers();
		
		List<UserResponse> userResponse 
						= users.stream()
						.map(user -> new UserResponse(
							user.getId(),
							user.getFirstName(),
							user.getEmail(), 
							List.of(user.getRole().getName())
						))
						.collect(Collectors.toList());		
		
		return new ResponseEntity<>(Map.of(
				"message", "Successfully get User List", 
				"UserList", userResponse ), HttpStatus.OK);
	}
}
