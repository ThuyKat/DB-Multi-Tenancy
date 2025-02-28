package multi_tenant.db.navigation.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import multi_tenant.db.navigation.Entity.Global.Developer;
import multi_tenant.db.navigation.Repository.Global.DeveloperRepository;

@Service
public class DeveloperService {

	@Autowired
	private DeveloperRepository developerRepository;
	
	public Developer getDeveloperByEmail(String email) {
		System.out.println("dev email:" + email);
		return developerRepository.findByEmail(email);
	}
}
