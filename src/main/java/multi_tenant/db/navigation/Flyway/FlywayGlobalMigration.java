//package multi_tenant.db.navigation.Flyway;
//
//import javax.sql.DataSource;
//
//import org.flywaydb.core.Flyway;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.stereotype.Component;
//
//@Configuration
//public class FlywayGlobalMigration {
//	@Autowired
//	public FlywayGlobalMigration(@Qualifier("globalDataSource") DataSource globalDataSource) {
//		System.out.println(globalDataSource);
//		Flyway flyway = Flyway.configure()
//				.dataSource(globalDataSource)
//				.locations("classpath:db/migration/global")
//				.load();
//		flyway.migrate();				
//	}
//}
