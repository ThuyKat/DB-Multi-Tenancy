//package multi_tenant.db.navigation.Utils;
//
//import java.io.Serializable;
//import javax.sql.DataSource;
//import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
////@Component
//public class MultiTenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    @Autowired
//    private TenantRoutingDataSource tenantRoutingDataSource;
//
//    @Override
//    protected DataSource selectAnyDataSource() {       
//        return tenantRoutingDataSource.getDefaultDataSource();
//    }
//
//    @Override
//    protected DataSource selectDataSource(Object tenantIdentifier) {
//        if (tenantIdentifier == null) {           
//            return tenantRoutingDataSource.getDefaultDataSource();
//        }
//
//        String tenant = tenantIdentifier.toString();        
//
//        DataSource dataSource = tenantRoutingDataSource.getDataSource(tenant);
//        if (dataSource == null) {           
//            return tenantRoutingDataSource.getDefaultDataSource();
//        }
//
//        return dataSource;
//    }
//}
