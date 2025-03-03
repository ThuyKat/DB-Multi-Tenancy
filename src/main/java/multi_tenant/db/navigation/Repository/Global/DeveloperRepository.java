package multi_tenant.db.navigation.Repository.Global;

import org.springframework.data.jpa.repository.JpaRepository;

import multi_tenant.db.navigation.Entity.Global.Developer;

public interface DeveloperRepository extends JpaRepository<Developer, Long>{
	Developer findByEmail(String email);

}
