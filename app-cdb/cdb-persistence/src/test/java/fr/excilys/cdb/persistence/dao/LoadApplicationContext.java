package fr.excilys.cdb.persistence.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
@ComponentScan("fr.excilys.cdb")
@PropertySource("application.properties")
public class LoadApplicationContext{

	@Value("${dataSource.driver}")
	private String h2Driver;
	@Value("${dataSource.url}")
	private String h2Url;
	@Value("${dataSource.username}")
	private String username;
	@Value("${dataSource.password}")
	private String password;

	@Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(h2Driver);
        dataSource.setUrl(h2Url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        
//        Resource initSchema = new ClassPathResource("1-SCHEMA.sql");
//        Resource initData = new ClassPathResource("2-ENTRIES.sql");
//        DatabasePopulator databasePopulator = new ResourceDatabasePopulator(initSchema, initData);
//        DatabasePopulatorUtils.execute(databasePopulator, dataSource);
        
        return dataSource;
    }
}
