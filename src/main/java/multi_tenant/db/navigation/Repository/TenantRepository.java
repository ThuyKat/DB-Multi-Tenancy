package multi_tenant.db.navigation.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import multi_tenant.db.navigation.Entity.Tenant;

public interface TenantRepository extends JpaRepository<Tenant, Long>{
	Tenant findByName(String name);
}
