package multi_tenant.db.navigation.Utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

@Component
public class MultiTenantDataSource extends AbstractRoutingDataSource{
	private final Map<Object, Object> dataSourceMap = new ConcurrentHashMap<>();	
	
	@Override
	protected Object determineCurrentLookupKey() {		
		return TenantContext.getCurrentTenant();
	}
	
    public void addDataSource(String databaseName, DataSource dataSource) {
        dataSourceMap.put(databaseName, dataSource);
        setTargetDataSources(dataSourceMap);
        afterPropertiesSet(); // Reload data sources dynamically
    }
}
