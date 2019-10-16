package fr.excilys.cdb.persistence.dao;

import static fr.excilys.cdb.persistence.dao.Configuration.PASSWORD;
import static fr.excilys.cdb.persistence.dao.Configuration.USER;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.excilys.cdb.persistence.models.Pageable;

public class ConnectionToDb {
	
	private Connection connection;

	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionToDb.class);
    
	public ConnectionToDb() {
        super();
    }
	
	public Optional<Connection> getConnection(){
		String url = null;
        LOGGER.info("Construction of url connection data base");
        try {
			if(connection == null || connection.isClosed()) {
				url = Configuration.getUrl();
				connection = DriverManager.getConnection(url, USER, PASSWORD);
				return Optional.of(connection);
						
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
        return Optional.of(connection);
	}
	
	public static void closeConnectionAndStetement(Connection conncetion, Statement statement) {
		try {
			if (statement != null && !statement.isClosed()) {
				statement.close();
				LOGGER.info("Statement Closed successfly...............!");
			}
	    } catch (SQLException e) {
	    		System.out.println(e.getMessage());
	    		LOGGER.info("Stetement Bad Closed...............!");
    	}
		try {
			if (conncetion != null && !conncetion.isClosed() ) {
				conncetion.close();
				LOGGER.info("Connexion Closed successfly...............!");
			}	
		} catch (SQLException e) {
				System.out.println(e.getMessage());
				LOGGER.info("Connexion Bad closed...............!");
		}
    }
	
	public static ResultSet prepareStetementAndExecureQuerytWithPage(Pageable page, PreparedStatement preparedStatement) throws SQLException {
    	int offset = (page.getNumber() - 1) * page.getSize();
    	int limit = page.getSize();
    	preparedStatement.setInt(1, offset);
    	preparedStatement.setInt(2, limit);
    	return preparedStatement.executeQuery();
    }
}
