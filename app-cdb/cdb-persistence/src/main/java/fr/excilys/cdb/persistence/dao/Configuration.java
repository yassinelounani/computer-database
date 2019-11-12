package fr.excilys.cdb.persistence.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration {

	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerDao.class);

    public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String PORT = "3306";
    public static final String PROTOCOLE_MYSQL = "jdbc:mysql:";
    public static final String HOST = "localhost";
    public static final String DATABASE = "computer-database-db";
    public static final String USER= "admincdb";
    public static final String PASSWORD= "qwerty1234";
    public static final String TIMEZONE = "serverTimezone=UTC";
    public static final String PROTOCOLE_H2 = "jdbc:h2:mem:";
    public static final String DATABASE_TEST = "test;";
    public static final String INIT_DB_H2 = "INIT=RUNSCRIPT FROM 'resources/1-SCHEMA.sql'";
    public static final String JDBC_DRIVER_H2 = "org.h2.Driver";
    public static final String SA = "sa";									  

    public static String getUrlForTest() {
    	StringBuilder url = new StringBuilder();
    	url.append(PROTOCOLE_H2)
	       .append(DATABASE_TEST)
	       .append(INIT_DB_H2);
    	return url.toString();
    }

    public static String getUrl() {
    	StringBuilder url = new StringBuilder();
        url.append(PROTOCOLE_MYSQL)
           .append("//")
           .append(HOST)
           .append(":")
           .append(PORT)
           .append("/")
           .append(DATABASE)
           .append("?")
           .append(TIMEZONE);
        return url.toString();
    }
}