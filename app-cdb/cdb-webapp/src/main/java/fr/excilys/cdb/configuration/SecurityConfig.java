package fr.excilys.cdb.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import fr.excilys.cdb.business.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
    private UserService userService;

    @Bean
    public UserDetailsService userDetailsServiceBean() {
        return userService;
    }

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
	  throws Exception {
	    auth.authenticationProvider(authenticationProvider());
	}

	@Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
           httpSecurity
                .authorizeRequests()
                .antMatchers("/computers").permitAll()
                .antMatchers("/computers/delete/**").hasRole("ADMIN")
                .antMatchers("/computers/add", "/computers/update").hasRole("ADMIN")
                .antMatchers("/computers/**").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .httpBasic();
           httpSecurity.csrf().disable();
    }

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(userDetailsServiceBean());
	    authProvider.setPasswordEncoder(encoder());
	    return authProvider;
	}

	@Bean
	public PasswordEncoder encoder() {
	    return new BCryptPasswordEncoder();
	}
}