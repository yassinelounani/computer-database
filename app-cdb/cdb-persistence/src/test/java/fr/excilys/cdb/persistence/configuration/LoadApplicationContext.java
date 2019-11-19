package fr.excilys.cdb.persistence.configuration;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ComponentScan(basePackages = {"fr.excilys.cdb.persistence.repositories","fr.excilys.cdb.persistence.models" })
@PropertySource("classpath:application.properties")
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
        
        return dataSource;
    }
}
