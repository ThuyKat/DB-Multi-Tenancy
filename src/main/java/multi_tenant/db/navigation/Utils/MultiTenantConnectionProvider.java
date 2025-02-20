package multi_tenant.db.navigation.Utils;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MultiTenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl{
	@Autowired
	private MultiTenantDataSource multiTenantDataSource;
	
	@Override
	protected DataSource selectAnyDataSource() {
		return multiTenantDataSource.getDefaultDataSource();
	}
	

	@Override
	protected DataSource selectDataSource(Object tenantIdentifier) {
		String currentTenant = TenantContext.getCurrentTenant();
		return multiTenantDataSource.getDataSource(currentTenant);
	}

}
