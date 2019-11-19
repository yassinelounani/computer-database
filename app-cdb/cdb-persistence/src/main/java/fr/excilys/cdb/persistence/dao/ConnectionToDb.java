package fr.excilys.cdb.persistence.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConnectionToDb {
	
	private Connection connection;
	
	@Autowired
	private DataSource dataSource;

	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionToDb.class);
	
	public  Optional<Connection> getConnection(){
        try {
			if(connection == null || connection.isClosed()) {
				connection = dataSource.getConnection();
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
}
