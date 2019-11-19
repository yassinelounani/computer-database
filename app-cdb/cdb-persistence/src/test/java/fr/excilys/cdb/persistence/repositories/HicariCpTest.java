package fr.excilys.cdb.persistence.repositories;

import static fr.excilys.cdb.persistence.dao.ConnectionToDb.closeConnectionAndStetement;
import static fr.excilys.cdb.persistence.mappers.Mapper.mapResultSetToComputer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
public class HicariCpTest {
	
    private static final String GET_ALL_COMPUTERS = "SELECT * FROM computer inner join company on computer.company_id = company.id";
	private static final String RESOURCES_DB_PROPERTIES = "resources/db.properties";
	private Connection connection = null;
    private HikariDataSource dataSource;
 
    @BeforeEach
    public void before() {
        HikariConfig configuration = new HikariConfig(RESOURCES_DB_PROPERTIES);
        dataSource = new HikariDataSource(configuration);
    }

    @Test
	public void test_val_expect_val2() {
    	PreparedStatement statement = null;
    	ResultSet resultSet = null;
    	try {
    		connection = dataSource.getConnection();
    		statement = connection.prepareStatement(GET_ALL_COMPUTERS);
    		resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println(mapResultSetToComputer(resultSet));
            }
        } catch (SQLException ex) {
        		System.out.println(ex.getMessage());
        } finally {
        		closeConnectionAndStetement(connection,statement);
        }
    }
}
