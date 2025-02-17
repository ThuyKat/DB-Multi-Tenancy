package multi_tenant.db.navigation.Interceptor;

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
	
	@Override 
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String shopName = request.getHeader("shopName");
		if(shopName != null) {
			String databaseName = tenantService.getDatabaseNameByShopId(shopName).getDbName();
			TenantContext.setCurrentTenant(databaseName);
		}
		return true;
	}
	@Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TenantContext.clear();
    }
	
}
