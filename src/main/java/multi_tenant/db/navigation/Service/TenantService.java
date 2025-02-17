package multi_tenant.db.navigation.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.transaction.Transactional;
import multi_tenant.db.navigation.Entity.Tenant;
import multi_tenant.db.navigation.Repository.TenantRepository;
import multi_tenant.db.navigation.Utils.DataSourceUtil;
import multi_tenant.db.navigation.Utils.MultiTenantDataSource;
@Service
public class TenantService {
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private MultiTenantDataSource multiTenantDataSource;
	
	@Autowired
	private TenantRepository tenantRepository;
	
	@Autowired
	private DataSourceUtil dataSourceUtil;
	
	public List<Tenant> getAllTenant(){
		return tenantRepository.findAll();
	}
	public Tenant getDatabaseNameByShopId(String shopName) {
		return tenantRepository.findByName(shopName);
	}
	
//	@Transactional //to rollback if error occurs
	public void createNewTenant(String shopName, Long ownerId) {
		String databaseName = shopName + "_db";
		
		try(Connection connection = dataSource.getConnection()){
			Statement statement = connection.createStatement();
			connection.setAutoCommit(false);
			
			//Add new tenant to global db: tenants table
			String insertNewTenantToGlobalDB = "INSERT INTO tenants (owner_id, name, db_name, status) VALUES (?, ?, ?, ?)";			
			try(PreparedStatement prepare = connection.prepareStatement(insertNewTenantToGlobalDB)){
				prepare.setLong(1, ownerId);
				prepare.setString(2, shopName);
				prepare.setString(3, databaseName);
				prepare.setString(4, "ACTIVE");
				prepare.executeUpdate();
			}
			
			//Create new tenant db
			statement.execute("CREATE DATABASE " + databaseName);
			
			//connect to new tenant db and run Flyway
			DataSource tenantDataSource = dataSourceUtil.createDataSource(databaseName);
			Flyway flyway = Flyway.configure().
					dataSource(tenantDataSource)
					.locations("classpath:db/migration/tenant")
					.baselineOnMigrate(false)
					.load();
			try {
				flyway.migrate();
			}catch (Exception e) {
				statement.execute("DROP DATABASE " + databaseName);
				throw new RuntimeException("Flyway migration failed for " + databaseName + ": " + e.getMessage());
			}			
			
			multiTenantDataSource.addDataSource(databaseName, tenantDataSource);
			
			connection.commit();			
					
		} catch (SQLException e) {
			throw new RuntimeException("Error when creating new tenant database: " + e.getMessage());
		}
	}

}
