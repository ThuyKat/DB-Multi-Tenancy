package multi_tenant.db.navigation.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import multi_tenant.db.navigation.JWT.JwtTokenProvider;

public class CustomUserDetailsService implements UserDetailsService{
	@Autowired
	private UserService userService;
	
	@Autowired
	private OwnerService ownerService;
	
	@Autowired
	private DeveloperService developerService;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Object user = findUserByEmail(email);
		
		return null;
	}
	  private Object findUserByEmail(String email) {
		  
		  
	  }
}
