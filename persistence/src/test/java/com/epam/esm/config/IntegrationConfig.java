package com.epam.esm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@Profile("dev")
public class IntegrationConfig {
    @Bean
    public DataSource embeddedDataSource(){
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL).addScript("script.sql").build();
    }
}
