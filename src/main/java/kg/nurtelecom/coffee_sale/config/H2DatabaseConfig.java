package kg.nurtelecom.coffee_sale.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "kg.nurtelecom.coffee_sale.service.jpa.h2",
        entityManagerFactoryRef = "h2EntityManagerFactory",
        transactionManagerRef = "h2TransactionManager"
)
public class H2DatabaseConfig {

    @Value("${spring.datasource.h2.url}")
    private String h2DatabaseUrl;

    @Value("${spring.datasource.h2.username}")
    private String h2DatabaseUsername;

    @Value("${spring.datasource.h2.password}")
    private String h2DatabasePassword;

    @Value("${spring.datasource.h2.driverClassName}")
    private String h2DatabaseDriverClassName;

    @Bean
    public DataSource h2DataSource() {
        return DataSourceBuilder.create()
                .url(h2DatabaseUrl)
                .username(h2DatabaseUsername)
                .password(h2DatabasePassword)
                .driverClassName(h2DatabaseDriverClassName)
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean h2EntityManagerFactory(
            EntityManagerFactoryBuilder entityManagerFactoryBuilder,
            @Qualifier("h2DataSource") DataSource dataSource) {
        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("kg.nurtelecom.coffee_sale.entity")
                .persistenceUnit("h2")
                .properties(hibernateProperties("org.hibernate.dialect.H2Dialect"))
                .build();
    }

    @Bean
    public JpaTransactionManager h2TransactionManager(
            @Qualifier("h2EntityManagerFactory") EntityManagerFactory entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    private Map<String, Object> hibernateProperties(String dialect) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", dialect);
        properties.put("hibernate.hbm2ddl.auto", "update");
        return properties;
    }
}

