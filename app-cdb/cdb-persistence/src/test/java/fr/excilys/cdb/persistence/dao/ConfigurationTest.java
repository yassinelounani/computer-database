package fr.excilys.cdb.persistence.dao;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ConfigurationTest {

	@Test
	public void test_getUrl_with_env_test_expect_url_H2() {
		//prepare
		System.setProperty("testing", "true");
		//execute
		String url = Configuration.getUrl();
		//verify
		assertThat(url).containsIgnoringCase(Configuration.JDBC_DRIVER_H2);
	}

	@Test
	public void test_getUrl_with_env_app_expect_url_mysql() {
		//prepare
		System.setProperty("testing", "false");
		//execute
		String url = Configuration.getUrl();
		//verify
		assertThat(url).containsIgnoringCase(Configuration.PROTOCOLE_MYSQL);
	}
}
