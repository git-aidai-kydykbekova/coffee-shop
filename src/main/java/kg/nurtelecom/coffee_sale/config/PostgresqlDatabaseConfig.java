package kg.nurtelecom.coffee_sale.config;


import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "kg.nurtelecom.coffee_sale.service.jpa.postgres",
        entityManagerFactoryRef = "postgresEntityManagerFactory",
        transactionManagerRef = "postgresTransactionManager"
)
public class PostgresqlDatabaseConfig {

    @Value("${spring.datasource.postgres.url}")
    private String postgresDatabaseUrl;

    @Value("${spring.datasource.postgres.username}")
    private String postgresDatabaseUsername;

    @Value("${spring.datasource.postgres.password}")
    private String postgresDatabasePassword;

    @Value("${spring.datasource.postgres.driverClassName}")
    private String postgresDatabaseDriverClassName;

    @Bean
    @Primary
    public DataSource postgresDataSource() {
        return DataSourceBuilder.create()
                .url(postgresDatabaseUrl)
                .username(postgresDatabaseUsername)
                .password(postgresDatabasePassword)
                .driverClassName(postgresDatabaseDriverClassName)
                .build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean postgresEntityManagerFactory(
            EntityManagerFactoryBuilder entityManagerFactoryBuilder,
            @Qualifier("postgresDataSource") DataSource dataSource) {
        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("kg.nurtelecom.coffee_sale.entity")
                .persistenceUnit("postgres")
                .properties(hibernateProperties("org.hibernate.dialect.PostgreSQLDialect"))
                .build();
    }

    @Bean
    @Primary
    public JpaTransactionManager postgresTransactionManager(
            @Qualifier("postgresEntityManagerFactory") EntityManagerFactory entityManagerFactory
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
