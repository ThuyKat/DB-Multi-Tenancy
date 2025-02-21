package multi_tenant.db.navigation.Utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


public class TenantRoutingDataSource extends AbstractRoutingDataSource{
	private final Map<Object, Object> dataSourceMap = new ConcurrentHashMap<>();
		
	@Override
	protected Object determineCurrentLookupKey() {	
		String tenant = TenantContext.getCurrentTenant();
		return tenant != null ? tenant : "default";
	}

	//hibernate
	// avoid null pointer execption if databaseName not exist
	public DataSource getDataSource(String databaseName) {
		return (DataSource) dataSourceMap.getOrDefault(databaseName, getDefaultDataSource()); //return value: dataSource
	}
	//hibernate
	public DataSource getDefaultDataSource() {
		return (DataSource) dataSourceMap.get("default"); //datasource of global db
	}
	
    public void addDataSource(String databaseName, DataSource dataSource) {
    	if(!dataSourceMap.containsKey(databaseName)) {
    		  dataSourceMap.put(databaseName, dataSource);        
    	        setTargetDataSources(dataSourceMap);
    	        afterPropertiesSet(); // Reload data sources dynamically
    	}      
    }
}
