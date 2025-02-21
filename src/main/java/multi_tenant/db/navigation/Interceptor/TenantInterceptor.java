package multi_tenant.db.navigation.Interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import multi_tenant.db.navigation.Service.TenantService;
import multi_tenant.db.navigation.Utils.TenantContext;

@Component
public class TenantInterceptor implements HandlerInterceptor{
	@Autowired
	private TenantService tenantService;
	private static final Logger logger = LoggerFactory.getLogger(TenantInterceptor.class);
	
	@Override 
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String shopName = request.getHeader("shop-name");
		System.out.println("Interceptor: " + shopName);
		if(shopName != null) {
			String databaseName = tenantService.getDatabaseNameByShopId(shopName).getDbName();
			TenantContext.setCurrentTenant(databaseName);
			logger.info("Tenant set to: {}", databaseName);
		}else {
			logger.warn("Shop-name header is missing");
		}
		return true;
	}
	@Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TenantContext.clear();
    }
	
}
