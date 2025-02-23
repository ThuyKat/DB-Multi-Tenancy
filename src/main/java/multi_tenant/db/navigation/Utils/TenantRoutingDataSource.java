package multi_tenant.db.navigation.Utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

//extends from parent abstract: AbstractDataSource - getConnection()
public class TenantRoutingDataSource extends AbstractRoutingDataSource {
	private final Map<Object, Object> dataSourceMap = new ConcurrentHashMap<>();

	@Override
	protected Object determineCurrentLookupKey() {
		// dbName
		String tenant = TenantContext.getCurrentTenant();
		System.out.println("Current Tenant in RoutingDataSource: " + (tenant != null ? tenant : "default"));
		System.out.println("Available DataSource: " + dataSourceMap.keySet());
		return tenant != null ? tenant : "default";
	}

	public void addDataSource(String databaseName, DataSource dataSource) {
		if (!dataSourceMap.containsKey(databaseName)) {
			this.dataSourceMap.put(databaseName, dataSource);
			super.setTargetDataSources(dataSourceMap);
			super.afterPropertiesSet(); // Reload data sources dynamically
			System.out.println("DataSourceMap size: " + dataSourceMap.size());

		}
	}
}
//TenantRoutingDataSource.getConnection():
//public Connection getConnection() {
//return determineTargetDataSource().getConnection(); //dataSource.getConnection();
//}
//protected DataSource determineTargetDataSource() {
//    Object lookupKey = determineCurrentLookupKey(); // Get tenant ID
//    DataSource dataSource = this.resolvedDataSources.get(lookupKey);
//    return (dataSource != null) ? dataSource : this.resolvedDefaultDataSource;
//}


