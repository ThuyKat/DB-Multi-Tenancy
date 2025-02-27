package multi_tenant.db.navigation.Repository.Global;

import org.springframework.data.jpa.repository.JpaRepository;

import multi_tenant.db.navigation.Entity.Global.Owner;

public interface OwnerRepository extends JpaRepository<Owner, Long>{
	Owner findByEmail(String email);
}
