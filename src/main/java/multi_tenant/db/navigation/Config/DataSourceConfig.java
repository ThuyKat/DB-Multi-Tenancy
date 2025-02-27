package multi_tenant.db.navigation.Config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;
import multi_tenant.db.navigation.Utils.DataSourceUtil;

@Configuration
@EnableJpaRepositories(
    basePackages = "multi_tenant.db.navigation.Repository.Global", //only scan respo in this package
    entityManagerFactoryRef = "globalEntityManagerFactory", 
    transactionManagerRef = "globalTransactionManager"
)
public class DataSourceConfig {

    @Autowired
    private DataSourceUtil dataSourceUtil;

    //1. create global datasource 
    @Bean(name = "globalDataSource")
    @Primary
    public DataSource globalDataSource() {
        return dataSourceUtil.createDataSource("db_navigation_global_multi_tenant");
    }
    
    //2 EntityManagerFactoryBuilder
    @Bean
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(), //set Hibernate as a JPA Provider
                new HashMap<>(), //show-sql, hibernate.ddl-auto
                null
        );
    }
    
    //3: create EntityManagerFactory (run after flyway)
    @Bean(name = "globalEntityManagerFactory")   
    public LocalContainerEntityManagerFactoryBean globalEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("globalDataSource") DataSource dataSource) {
    	//return EntityManagerFactory
        return builder
                .dataSource(dataSource)
                .packages("multi_tenant.db.navigation.Entity.Global") //only scan Entities inside this package
                .persistenceUnit("globalPU")
                .build();
    }

    //4: create TransactionManager for Global Database - CRUD with db @Transactional-> rollback
    @Bean(name = "globalTransactionManager")
    public PlatformTransactionManager globalTransactionManager(
            @Qualifier("globalEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
