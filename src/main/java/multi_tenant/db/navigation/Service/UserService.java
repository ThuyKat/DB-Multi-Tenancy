package multi_tenant.db.navigation.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import multi_tenant.db.navigation.Entity.Tenant.User;
import multi_tenant.db.navigation.Repository.Tenant.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRespository;
	
//	@Transactional(transactionManager = "tenantTransactionManager")
	public User getUserByEmailWithPermission(String email) {
		return userRespository.findByEmailWithPermissions(email);
	}
	
	public User getUserByEmail(String email) {
		return userRespository.findByEmail(email);
	}
}
