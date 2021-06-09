package com.reply.batch.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;

@Configuration
public class DatasourceConfig {

    @Bean
    @Primary
    public HikariDataSource datasource(DatasourceProperties properties){
        HikariConfig config = new HikariConfig();
        config.setConnectionTestQuery(properties.getTestQuery());
        config.setDataSourceClassName(properties.getDriver());
        config.setMaximumPoolSize(2);
        config.addDataSourceProperty("driverType",4);
        config.addDataSourceProperty("serverName","localhost");
        config.addDataSourceProperty("user",properties.getUsername());
        config.addDataSourceProperty("password", properties.getPassword());
        config.addDataSourceProperty("databaseName", properties.getDatabaseName());
        config.addDataSourceProperty("portNumber", properties.getPortNumber());
        return new HikariDataSource(config);
    }

}
