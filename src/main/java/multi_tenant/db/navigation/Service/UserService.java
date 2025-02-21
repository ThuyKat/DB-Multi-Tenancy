package multi_tenant.db.navigation.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import multi_tenant.db.navigation.Entity.Tenant.User;
import multi_tenant.db.navigation.Repository.Tenant.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRespository;
	
	public User getUserByEmail(String email) {
		return userRespository.findByEmail(email);
	}
}
