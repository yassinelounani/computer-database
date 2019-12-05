package fr.excilys.cdb.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable();

		httpSecurity
			.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		httpSecurity
				.authorizeRequests()
				.antMatchers("/login").permitAll()
				.antMatchers("/computers/**").permitAll()
				.antMatchers("/companies/**").permitAll()
				.antMatchers("/cdb-webapp/swagger-ui.html").permitAll()
				.antMatchers("/**/webjars/**").permitAll()
				.antMatchers("/**/configuration/ui").permitAll()
				.antMatchers("/**/configuration/security").permitAll()
				.antMatchers("/csrf").permitAll();
//				.antMatchers("/computers/delete/**").hasRole("ADMIN")
//				.antMatchers("/computers/add", "/computers/update").hasRole("ADMIN")
//				.antMatchers("/computers/**").hasRole("USER")
				//.anyRequest().authenticated();

	}

	@Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/**/swagger-resources/configuration/ui","/**/configuration/ui", "/**/swagger-resources",
                 "/**/swagger-ui.html", "/**/webjars/**", "/**/configuration/security", "/csrf","/index.jsp", "/v2/api-docs");
    }

}