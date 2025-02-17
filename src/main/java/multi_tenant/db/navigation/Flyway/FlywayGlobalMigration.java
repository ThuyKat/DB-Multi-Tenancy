package multi_tenant.db.navigation.Flyway;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FlywayGlobalMigration {
	@Autowired
	public FlywayGlobalMigration(DataSource globalDataSource) {
		Flyway flyway = Flyway.configure()
				.dataSource(globalDataSource)
				.locations("classpath:db/migration/global")
				.load();
		flyway.migrate();				
	}
}
