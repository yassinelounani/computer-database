package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static dao.Configuration.JDBC_DRIVER;
import static dao.Configuration.PASSWORD;
import static dao.Configuration.USER;

public class ConnectionToDb {


    public ConnectionToDb() {
        super();
    }

    public Connection getConnectionDb() throws ClassNotFoundException, SQLException{
    	
        String url = Configuration.getUrl();
        return DriverManager.getConnection(url, USER, PASSWORD); 
    }
}
