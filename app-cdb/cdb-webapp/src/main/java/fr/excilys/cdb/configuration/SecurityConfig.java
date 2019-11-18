package fr.excilys.cdb.configuration;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String SELECT_USERNAME_ROLE = "select username, role from user_roles where username=?";
	private static final String SELECT_USERNAME_PASSWORD = "select username,password, enabled from users where username=?";
	@Autowired
	DataSource dataSource;
	
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
			.usersByUsernameQuery(SELECT_USERNAME_PASSWORD)
			.authoritiesByUsernameQuery(SELECT_USERNAME_ROLE);
	}	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/admin/**").access("hasRole('ADMIN')")
			.and()
			.formLogin().loginPage("/login").failureUrl("/login?error")
			.usernameParameter("username").passwordParameter("password")
			.and()
			.logout().logoutSuccessUrl("/login?logout")
			.and()
			.exceptionHandling().accessDeniedPage("/403")
			.and()
			.csrf();
	}
}