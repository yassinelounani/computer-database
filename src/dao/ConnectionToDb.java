package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import static dao.Configuration.PASSWORD;
import static dao.Configuration.USER;

public class ConnectionToDb {

	private static final Logger LOGGER = Logger.getLogger(ConnectionToDb.class);
    
	public ConnectionToDb() {
        super();
    }

    public Connection getConnectionDb() throws ClassNotFoundException, SQLException{
        String url = Configuration.getUrl();
        LOGGER.info("Construction of url connection data base");
        return DriverManager.getConnection(url, USER, PASSWORD); 
    }
}
