package multi_tenant.db.navigation.Config;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import multi_tenant.db.navigation.Entity.Tenant;
import multi_tenant.db.navigation.Service.TenantService;
import multi_tenant.db.navigation.Utils.DataSourceUtil;
import multi_tenant.db.navigation.Utils.MultiTenantDataSource;

@Configuration
public class DataSourceConfig {

	@Autowired
	TenantService tenantService;	
	
	@Autowired
	private MultiTenantDataSource multiTenantDataSource;
	
	@Autowired
	private DataSourceUtil dataSourceUtil;
//	private final Map<Object, Object> dataSourceMap = new ConcurrentHashMap<>();	
	
	@Bean
	public DataSource dataSource() {
		List<Tenant> tenants = tenantService.getAllTenant();
		for(Tenant tenant: tenants) {
			multiTenantDataSource.addDataSource(tenant.getDbName(), dataSourceUtil.createDataSource(tenant.getDbName()));
		}	
		multiTenantDataSource.setDefaultTargetDataSource(dataSourceUtil.createDataSource("global_multi_tenant"));
		multiTenantDataSource.afterPropertiesSet(); //reload database list
		
		
		return multiTenantDataSource;
	}
	
}
