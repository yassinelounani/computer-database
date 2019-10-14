package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import org.apache.log4j.Logger;

import api.Page;

import static dao.Configuration.PASSWORD;
import static dao.Configuration.USER;

public class ConnectionToDb {

	private static final Logger LOGGER = Logger.getLogger(ConnectionToDb.class);
    
	public ConnectionToDb() {
        super();
    }

	public Optional<Connection> getConnectionDb() {
		Connection connection = null;
		String url = Configuration.getUrl();
        LOGGER.info("Construction of url connection data base");
        try {
        	 connection = DriverManager.getConnection(url, USER, PASSWORD);
        	 return Optional.of(connection);
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        	return Optional.empty();
        }
    }
	
	public static void closeConnectionAndStetement(Connection conncetion, Statement statement) {
		try {
			if (statement != null) {
				statement.close();
				LOGGER.info("Statement Closed successfly...............!");
			}
	    } catch (SQLException e) {
	    		System.out.println(e.getMessage());
	    		LOGGER.info("Stetement Bad Closed...............!");
    	}
		try {
			if (conncetion != null ) {
				conncetion.close();
				LOGGER.info("Connexion Closed successfly...............!");
			}	
		} catch (SQLException e) {
				System.out.println(e.getMessage());
				LOGGER.info("Connexion Bad closed...............!");
		}
    }
	
	public static ResultSet prepareStetementAndExecureQuerytWithPage(Page page, PreparedStatement preparedStatement) throws SQLException {
    	int offset = (page.getNumber() - 1) * page.getSize();
    	int limit = page.getSize();
    	preparedStatement.setInt(1, offset);
    	preparedStatement.setInt(2, limit);
    	return preparedStatement.executeQuery();
    }
}
