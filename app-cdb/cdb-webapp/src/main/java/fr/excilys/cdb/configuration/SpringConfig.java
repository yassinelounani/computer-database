package fr.excilys.cdb.configuration;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"fr.excilys.cdb.controllers", "fr.excilys.cdb", })
@PropertySource("classpath:application.properties")
public class SpringConfig implements WebApplicationInitializer {

	
	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;
	@Value("${spring.datasource.driver-class-name}")
	private String jdbcDriver;

	@Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(jdbcDriver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
 
        return dataSource;
    }

	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
		return new NamedParameterJdbcTemplate(dataSource());
	}

	@Override
	public void onStartup(ServletContext ctx) throws ServletException {
        AnnotationConfigWebApplicationContext webCtx = new AnnotationConfigWebApplicationContext();
        webCtx.register(SpringConfig.class, SpringWebMvcconfig.class);
        webCtx.setServletContext(ctx);
        ServletRegistration.Dynamic servlet = ctx.addServlet("dispatcher", new DispatcherServlet(webCtx));
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");
    }

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
	      LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
	      em.setDataSource(dataSource());
	      em.setPackagesToScan(new String[] { "fr.excilys.cdb.persistence" });
	 
	      JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	      em.setJpaVendorAdapter(vendorAdapter);
	      em.setJpaProperties(additionalProperties());
	 
	      return em;
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
	    JpaTransactionManager transactionManager = new JpaTransactionManager();
	    transactionManager.setEntityManagerFactory(emf);
	 
	    return transactionManager;
	}

	Properties additionalProperties() {
	    Properties properties = new Properties();
	    properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");    
	    return properties;
	}
}
