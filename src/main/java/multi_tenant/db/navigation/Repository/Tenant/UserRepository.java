package multi_tenant.db.navigation.Repository.Tenant;

import org.springframework.data.jpa.repository.JpaRepository;

import multi_tenant.db.navigation.Entity.Tenant.User;

public interface UserRepository extends JpaRepository<User, Long>{
	User findByEmail(String email);
}
