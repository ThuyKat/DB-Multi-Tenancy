package multi_tenant.db.navigation.Repository.Global;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import multi_tenant.db.navigation.Entity.Global.Tenant;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
	Tenant findByName(String name);
	
	List<Tenant> findByOwnerId(Long ownerId);
}
