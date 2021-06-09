package com.reply.batch.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "db")
@Configuration("DatasourceProperties")
public class DatasourceProperties {
    String driver;
    String url;
    String databaseName;
    String portNumber;
    String username;
    String password;
    String testQuery;
}
