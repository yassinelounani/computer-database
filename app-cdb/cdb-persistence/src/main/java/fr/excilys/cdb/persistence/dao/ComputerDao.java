package fr.excilys.cdb.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.excilys.cdb.persistence.mappers.HelperDate;
import fr.excilys.cdb.persistence.mappers.Mapper;
import static fr.excilys.cdb.persistence.dao.ConnectionToDb.closeConnectionAndStetement;
import fr.excilys.cdb.persistence.models.ComputerEntity;
import fr.excilys.cdb.persistence.models.Pageable;


public class ComputerDao implements Dao {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerDao.class);
	
	private static final String GET_ALL_COMPUTERS = 
			  "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, computer.company_id, company.name "
			+ "FROM computer LEFT JOIN company ON computer.company_id = company.id;";
	
	private static final String GET_ALL_COMPUTERS_WITH_PAGE = 
			  "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, computer.company_id, company.name "
			+ "FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "LIMIT ?, ?";
		
	private static final String GET_COMPUTER_BY_ID = 
			  "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, computer.company_id, company.name "
			+ "FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "WHERE computer.id=?;";
	
	private static final String INSERT_COMPUTER = 
			  "INSERT INTO computer(id, name, introduced, discontinued, company_id) "
			+ "VALUES(?, ?, ?, ?, ?);";
	
	private static final String UPDATE_COMPUTER = 
			  "UPDATE computer "
			+ "SET name = ?, introduced = ?, discontinued = ?, company_id = ? "
			+ "WHERE id = ?";
	
	private static final String DELETE_COMPUTER =
			  "DELETE FROM computer "
		    + "WHERE computer.id =?";
	
	private static final int NO_CONNECTION = -1;
	
	private static final boolean IS_UPDATE = true;
	
	private static final boolean IS_INSERT = false;
	
	private ConnectionToDb connexionToDb;
    
	private ComputerDao(ConnectionToDb connexionToDb) {
        super();
        this.connexionToDb = connexionToDb;
    }

    private static ComputerDao INSTANCE = null;

    public static synchronized ComputerDao getInstance(ConnectionToDb connexionToDb) {
        if (INSTANCE == null) {
            INSTANCE = new ComputerDao(connexionToDb);
        }
        return INSTANCE;
    }

    public List<ComputerEntity> getComputers() {
    	Statement statement = null;
    	List<ComputerEntity> computers = new ArrayList<>();
    	Optional<Connection>  connection = connexionToDb.getConnectionDb();
    	if(!connection.isPresent()) return computers;
    	LOGGER.info("connection well-established to the database ...................");	
    	try {
		    	statement = connection.get().createStatement();
		    	LOGGER.info("query : " + GET_ALL_COMPUTERS);
		    	ResultSet results = statement.executeQuery(GET_ALL_COMPUTERS);
		    	while (results.next()) {
		    	   computers.add(Mapper.mapResultSetToComputer(results)); 			
		    	}	
    	} catch (SQLException e) {
    			System.out.println(e.getMessage());
    			computers.clear();
    	} finally {
    		closeConnectionAndStetement(connection.get(), statement);
    	}
    	return computers;
    }
    
    public List<ComputerEntity> getComputersWithPage(Pageable page) {
    	List<ComputerEntity> computers = new ArrayList<>();
    	Optional<Connection> connection = connexionToDb.getConnectionDb();
    	PreparedStatement preparedStatement = null;
    	if(!connection.isPresent()) return computers;
    	LOGGER.info("connection well-established to the database ...................");
    	try {
    			LOGGER.info("query : " + GET_ALL_COMPUTERS_WITH_PAGE );
		    	preparedStatement  = connection.get().prepareStatement(GET_ALL_COMPUTERS_WITH_PAGE);
		    	ResultSet results = prepareStetementAndExecureQuerytWithPage(page, preparedStatement);
		    	while (results.next()) { 
		    		computers.add(Mapper.mapResultSetToComputer(results));
		    	}
		    	LOGGER.info("List computer is successfly loaded");
		} catch (SQLException e) {
				System.out.println(e.getMessage());
				computers.clear();
		} finally {
				closeConnectionAndStetement(connection.get(), preparedStatement);
		}
    	return computers;
    }
    
    public Optional<ComputerEntity> getComputerById(long id) {
    	ComputerEntity computer = null;
    	PreparedStatement preparedStatement = null;
    	Optional<Connection> connection = connexionToDb.getConnectionDb();
    	if(!connection.isPresent()) return Optional.empty();
    	LOGGER.info("connection well-established to the database ...................");
    	try {
		    	LOGGER.info("query : "+ GET_COMPUTER_BY_ID);
		    	preparedStatement  = connection.get().prepareStatement(GET_COMPUTER_BY_ID);
		    	preparedStatement.setLong(1, id);
		    	ResultSet results = preparedStatement.executeQuery();
		    	while (results.next()) { 
		    		computer = Mapper.mapResultSetToComputer(results);
		    	}
		} catch (SQLException e) {
				System.out.println(e.getMessage());
		} finally {
				closeConnectionAndStetement(connection.get(), preparedStatement);
		}
    	return Optional.ofNullable(computer);
    }
    
    public int addComputer(ComputerEntity computer) {
    	return addComputerOrUpdateIt(computer, IS_INSERT);
    }
    
    public int updateComputer(ComputerEntity computer) {
    	return addComputerOrUpdateIt(computer, IS_UPDATE);
    }
    
    public int deleteComputerById(long id) {
    	int executeDelete = 0;
    	Optional<Connection> connection = connexionToDb.getConnectionDb();
    	if(!connection.isPresent()) return NO_CONNECTION;
    	LOGGER.info("connection well-established to the database ...................");
    	PreparedStatement preparedStatement = null;
    	try {
	    		LOGGER.info("query : "+ DELETE_COMPUTER);
	    		preparedStatement  = connection.get().prepareStatement(DELETE_COMPUTER);
	    		preparedStatement.setLong(1, id);
	    		executeDelete = preparedStatement.executeUpdate();
    	} catch (SQLException e) {
    			System.out.println(e.getMessage());
		} finally {
				closeConnectionAndStetement(connection.get(), preparedStatement);
		}
    	return executeDelete;	
    }

    private int addComputerOrUpdateIt(ComputerEntity computer, boolean isUpdate) {
    	int executeUpdate = 0;
    	Optional<Connection> connection = connexionToDb.getConnectionDb();
    	if(!connection.isPresent()) return NO_CONNECTION;
    	LOGGER.info("connection well-established to the database ...................");
    	PreparedStatement preparedStatement = null;
    	try {
    			executeUpdate = preparStatementAndExecuteUpdate(computer, connection.get(), isUpdate);
		} catch (SQLException e) {
				System.out.println(e.getMessage());
		} finally {
				closeConnectionAndStetement(connection.get(), preparedStatement);
		}
    	return executeUpdate;
    }
    
    private ResultSet prepareStetementAndExecureQuerytWithPage(Pageable page, PreparedStatement preparedStatement) throws SQLException {
    	int offset = (page.getNumber() - 1) * page.getSize();
    	int limit = page.getSize();
    	preparedStatement.setInt(1, offset);
    	preparedStatement.setInt(2, limit);
    	return preparedStatement.executeQuery();
    }
    
    private int preparStatementAndExecuteUpdate(ComputerEntity computer, Connection connection, boolean isUpdate) throws SQLException {
    	int index = 1;
    	String query = INSERT_COMPUTER;
    	if(isUpdate) {
    		index = 0;
    		query = UPDATE_COMPUTER;
    	}
    	LOGGER.info("query : "+ query);
    	PreparedStatement preparedStatement  = connection.prepareStatement(query);
    	if(! isUpdate) preparedStatement.setLong(index, computer.getId());
    	preparedStatement.setString(index + 1 , computer.getName());
    	preparedStatement.setDate(index + 2, HelperDate.dateToSql(computer.getDateIntroduced()));
    	preparedStatement.setDate(index + 3, HelperDate.dateToSql(computer.getDateDiscontinued()));
    	if(computer.getCompany().getId() > 0) {
    		preparedStatement.setLong(index + 4, computer.getCompany().getId());
    	} else {
    		preparedStatement.setNull(index + 4, Types.DATE);
    	}
    	if (isUpdate) {
    		preparedStatement.setLong(index + 5, computer.getId());
    	}
    	return preparedStatement.executeUpdate();
    }
    
}
