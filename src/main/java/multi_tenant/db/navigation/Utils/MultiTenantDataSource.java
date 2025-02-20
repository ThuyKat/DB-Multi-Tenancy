package multi_tenant.db.navigation.Utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


public class MultiTenantDataSource extends AbstractRoutingDataSource{
	private final Map<Object, Object> dataSourceMap = new ConcurrentHashMap<>();
		
	@Override
	protected Object determineCurrentLookupKey() {	
		String tenant = TenantContext.getCurrentTenant();
		return tenant != null ? tenant : "default";
	}

	//hibernate
	public DataSource getDataSource(String databaseName) {
		return (DataSource) dataSourceMap.get(databaseName); //return value: dataSource
	}
	//hibernate
	public DataSource getDefaultDataSource() {
		return (DataSource) dataSourceMap.get("default"); //datasource of global db
	}
	
    public void addDataSource(String databaseName, DataSource dataSource) {
        dataSourceMap.put(databaseName, dataSource);        
        setTargetDataSources(dataSourceMap);
        afterPropertiesSet(); // Reload data sources dynamically
    }
}
