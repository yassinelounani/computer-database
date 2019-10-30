package fr.excilys.cdb.persistence.dao;

import static fr.excilys.cdb.persistence.dao.ConnectionToDb.closeConnectionAndStetement;
import static fr.excilys.cdb.persistence.dao.ConnectionToDb.prepareStetementAndExecureQuerytWithPage;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.excilys.cdb.persistence.mappers.HelperDate;
import fr.excilys.cdb.persistence.mappers.Mapper;
import fr.excilys.cdb.persistence.models.ComputerEntity;
import fr.excilys.cdb.persistence.models.Pageable;
import fr.excilys.cdb.persistence.models.SortDao;
@Component
public class ComputerDao implements Dao {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerDao.class);

	private static final String GET_ALL_COMPUTERS = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, company.id, company.name "
			+ "FROM computer LEFT JOIN company ON computer.company_id = company.id ORDER BY computer.name, company.name ;";

	private static final String GET_TOTAL_PAGES = "SELECT COUNT(computer.id) "
			+ "FROM computer LEFT JOIN company ON computer.company_id = company.id;";

	private static final String GET_ALL_COMPUTERS_WITH_PAGE = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, company.id, company.name "
			+ "FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "ORDER BY ";
	private static final String ORDER_BY = "computer.name ";
	private static final String LIMIT = "LIMIT ?, ?";
	

	private static final String GET_COMPUTER_BY_ID = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, company.id, company.name "
			+ "FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "WHERE computer.id=?;";

	private static final String INSERT_COMPUTER = "INSERT INTO computer(id, name, introduced, discontinued, company_id) "
			+ "VALUES(?, ?, ?, ?, ?);";

	private static final String UPDATE_COMPUTER = "UPDATE computer "
			+ "SET name = ?, introduced = ?, discontinued = ?, company_id = ? "
			+ "WHERE id = ?";

	private static final String DELETE_COMPUTER = "DELETE FROM computer "
		    + "WHERE computer.id = ?";

	private static final String DELETE_COMPANY = "DELETE FROM company "
		    + "WHERE company.id = ?";

	private static final String DELETE_COMPUTER_BY_COMPANY = "DELETE FROM computer "
		    + "WHERE company_id = ?";

	private static final String GET_MAX = "SELECT MAX(computer.id) as max FROM computer";
	
	private static final String SERACH_NAME = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, company.id, company.name "
			+ "FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "WHERE company.name LIKE ? OR computer.name LIKE ? "
			+ "ORDER BY computer.name, company.name "
			+ "LIMIT ?, ?";

	private static final String SERACH_TOTAL_COMPUTERS = "SELECT COUNT(computer.id) "
			+ "FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "WHERE company.name LIKE ? OR computer.name LIKE ? ";

	private static final int NO_CONNECTION = -1;
	private static final boolean IS_UPDATE = true;
	private static final boolean IS_INSERT = false;
	
	@Autowired
	private ConnectionToDb connectionToDb;

//	private ComputerDao() {
//        super();
//        //this.connectionToDb = ConnectionToDb.getInstance();
//    }
	
	public void setConnectionToDb(ConnectionToDb connectionToDb) {
		this.connectionToDb = connectionToDb;
	}

//	private static ComputerDao instance = null;
//
//    public static synchronized ComputerDao getInstance() {
//        if (instance == null) {
//            instance = new ComputerDao();
//        }
//        return instance;
//    }

    public List<ComputerEntity> getComputers() {
    	Statement statement = null;
    	Optional<Connection> connection = connectionToDb.getConnection();
    	List<ComputerEntity> computers = new ArrayList<>();
    	try {
	    	if (!connection.isPresent()) {
	    		return computers;
	    	}
	    	LOGGER.info("connection well-established to the database ...................");
		    	statement = connection.get().createStatement();
		    	LOGGER.info("query : {}", GET_ALL_COMPUTERS);
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
    	Optional<Connection> connection = connectionToDb.getConnection();
    	PreparedStatement preparedStatement = null;
    	if (!connection.isPresent()) {
    		return computers;
    	}
    	LOGGER.info("connection well-established to the database...................");
    	try {
    		String query = GET_ALL_COMPUTERS_WITH_PAGE + ORDER_BY + LIMIT;
    		LOGGER.info("query : {}", query);
		    preparedStatement  = connection.get().prepareStatement(query);
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

    public List<ComputerEntity> getComputersWithPageAndSort(Pageable page, SortDao sort) {
    	List<ComputerEntity> computers = new ArrayList<>();
    	StringBuilder query = new StringBuilder();
    	Optional<Connection> connection = connectionToDb.getConnection();
    	PreparedStatement preparedStatement = null;
    	if (!connection.isPresent()) {
    		return computers;
    	}
    	LOGGER.info("connection well-established to the database...................");
    	try {
    		query.append(GET_ALL_COMPUTERS_WITH_PAGE).append(getSortQuery(sort)).append(LIMIT);
    		LOGGER.info("query : {}", query.toString());
		    preparedStatement  = connection.get().prepareStatement(query.toString());
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

    public List<ComputerEntity> getSerchComputersWithPage(Pageable page, String name) {
    	List<ComputerEntity> computers = new ArrayList<>();
    	Optional<Connection> connection = connectionToDb.getConnection();
    	PreparedStatement preparedStatement = null;
    	if (!connection.isPresent()) {
    		return computers;
    	}
    	LOGGER.info("connection well-established to the database...................");
    	try {
    		LOGGER.info("query : {}", SERACH_NAME);
		    preparedStatement  = connection.get().prepareStatement(SERACH_NAME);
		    ResultSet results = prepareStetementAndSearchWithPage(page, name, preparedStatement);
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
    public long totalOfelements() {
    	return aggregatedOperation(GET_TOTAL_PAGES);
    }

    public Optional<ComputerEntity> getComputerById(long id) {
    	ComputerEntity computer = null;
    	PreparedStatement preparedStatement = null;
    	Optional<Connection> connection = connectionToDb.getConnection();
    	if (!connection.isPresent()) {
    		return Optional.empty();
    	}
    	LOGGER.info("connection well-established to the database ...................");
    	try {
		    	LOGGER.info("query : {}", GET_COMPUTER_BY_ID);
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
    	Optional<Connection> connection = connectionToDb.getConnection();
    	if (!connection.isPresent()) {
    		return NO_CONNECTION;
    	}
    	LOGGER.info("connection well-established to the database ...................");
    	PreparedStatement preparedStatement = null;
    	try {	
	    		LOGGER.info("query : {}", DELETE_COMPUTER);
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

    public int deleteCompany(long companyId) {
    	int executeDelete = 0;
    	Optional<Connection> connection = connectionToDb.getConnection();
    	if (!connection.isPresent()) {
    		return NO_CONNECTION;
    	}
    	LOGGER.info("connection well-established to the database ...................");
    	try {	
    			connection.get().setAutoCommit(false);
	    		prepareAndExecute(companyId, connection.get(), DELETE_COMPUTER_BY_COMPANY);
	    		executeDelete = prepareAndExecute(companyId, connection.get(), DELETE_COMPANY);
	    		connection.get().commit();
	    		connection.get().setAutoCommit(true);
    	} catch (SQLException e) {
    		try {
				connection.get().rollback();
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}	
    		System.out.println(e.getMessage());
		} finally {
				closeConnectionAndStetement(connection.get(), null);
		}
    	return executeDelete;
    }

	private int prepareAndExecute(long id, Connection connection, String query) throws SQLException {
		int executeDelete;
		PreparedStatement preparedStatement;
		LOGGER.info("query : {}", query);
		preparedStatement  = connection.prepareStatement(query);
		preparedStatement.setLong(1, id);
		executeDelete = preparedStatement.executeUpdate();
		return executeDelete;
	}

    private int addComputerOrUpdateIt(ComputerEntity computer, boolean isUpdate) {
    	int executeUpdate = 0;
    	Optional<Connection> connection = connectionToDb.getConnection();
    	if (!connection.isPresent()) {
    		return NO_CONNECTION;
    	}
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

    private int preparStatementAndExecuteUpdate(ComputerEntity computer, Connection connection, boolean isUpdate) throws SQLException {
    	int index = 1;
    	String query = INSERT_COMPUTER;
    	if (isUpdate) {
    		index = 0;
    		query = UPDATE_COMPUTER;
    	}
    	LOGGER.info("query : {}", query);
    	PreparedStatement preparedStatement  = connection.prepareStatement(query);
    	if (!isUpdate) {
    		preparedStatement.setLong(index, computer.getId());
    	}
    	preparedStatement.setString(index + 1, computer.getName());
    	preparedStatement.setDate(index + 2, HelperDate.dateToSql(computer.getIntroduced()));
    	preparedStatement.setDate(index + 3, HelperDate.dateToSql(computer.getDiscontinued()));
    	if (computer.getCompany().getId() > 0) {
    		preparedStatement.setLong(index + 4, computer.getCompany().getId());
    	} else {
    		preparedStatement.setNull(index + 4, Types.DATE);
    	}
    	if (isUpdate) {
    		preparedStatement.setLong(index + 5, computer.getId());
    	}
    	return preparedStatement.executeUpdate();
    }

    public long getMaxIdComputer() {
    	return aggregatedOperation(GET_MAX);
    }

    private long aggregatedOperation(String query) {
    	Optional<Connection> connection = connectionToDb.getConnection();
    	Statement statement = null;
    	long value = 0;
    	if (!connection.isPresent()) {
    		return NO_CONNECTION;
    	}
    	LOGGER.info("connection well-established to the database...................");
    	try {
    		statement = connection.get().createStatement();
	    	LOGGER.info("query : {}", query);
	    	ResultSet results = statement.executeQuery(query);
	    	if (results.first()) {
	    		value = results.getLong(1);
	    		LOGGER.info("GET AGGREGATION....................................");
	    	}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnectionAndStetement(connection.get(), statement);
		}
    	return value;
    }

    public long totalComputersFounded(String name) {
    	Optional<Connection> connection = connectionToDb.getConnection();
    	PreparedStatement preparedStatement = null;
    	long value = 0;
    	if (!connection.isPresent()) {
    		return NO_CONNECTION;
    	}
    	LOGGER.info("connection well-established to the database...................");
    	try {
    		preparedStatement = connection.get().prepareStatement(SERACH_TOTAL_COMPUTERS);
    		ResultSet results = prepareStetementCountSearch(name,preparedStatement);
	    	if (results.first()) {
	    		value = results.getLong(1);
	    		LOGGER.info("GET AGGREGATION.......................................");
	    	}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnectionAndStetement(connection.get(), preparedStatement);
		}
    	return value;
    }

    private ResultSet prepareStetementAndSearchWithPage(Pageable page, String name, PreparedStatement preparedStatement) throws SQLException {
    	int offset = (page.getNumber() - 1) * page.getSize();
    	int limit = page.getSize();
    	prepareNameForSearch(name, preparedStatement);
    	preparedStatement.setInt(3, offset);
    	preparedStatement.setInt(4, limit);
    	return preparedStatement.executeQuery();
    }

    private ResultSet prepareStetementCountSearch(String name, PreparedStatement preparedStatement) throws SQLException {
    	prepareNameForSearch(name, preparedStatement);
    	return preparedStatement.executeQuery();
    }

	private void prepareNameForSearch(String name, PreparedStatement preparedStatement) throws SQLException {
		String nameRequest = "%"+name.trim()+"%";
    	preparedStatement.setString(1, nameRequest);
    	preparedStatement.setString(2, nameRequest);
	}
	private String getSortQuery(SortDao sort) {
		StringBuilder orderBy = new StringBuilder();
		orderBy.append(sort.getProperty())
			   .append(".name")
			   .append(" ")
			   .append(sort.getOrder().getSort())
			   .append(" ");
		return orderBy.toString();
	}

}