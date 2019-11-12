package fr.excilys.cdb.persistence.dao;

import static fr.excilys.cdb.persistence.dao.ConnectionToDb.closeConnectionAndStetement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import fr.excilys.cdb.persistence.mappers.ComputerRowMapper;
import fr.excilys.cdb.persistence.mappers.HelperDate;
import fr.excilys.cdb.persistence.models.ComputerEntity;
import fr.excilys.cdb.persistence.models.Pageable;
import fr.excilys.cdb.persistence.models.SortDao;

@Component
public class ComputerDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerDao.class);

	private static final String GET_ALL_COMPUTERS = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, company.id, company.name "
			+ "FROM computer LEFT JOIN company ON computer.company_id = company.id ORDER BY computer.name, company.name";

	private static final String GET_TOTAL_PAGES = "SELECT COUNT(computer.id) "
			+ "FROM computer LEFT JOIN company ON computer.company_id = company.id";

	private static final String GET_ALL_COMPUTERS_WITH_PAGE = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, company.id, company.name "
			+ "FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "ORDER BY ";
	private static final String ORDER_BY = "computer.name ";
	private static final String LIMIT = "LIMIT :offset, :limit";

	private static final String GET_COMPUTER_BY_ID = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, company.id, company.name "
			+ "FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "WHERE computer.id= :id";

	private static final String INSERT_COMPUTER = "INSERT INTO computer(id, name, introduced, discontinued, company_id) "
			+ "VALUES(?, ?, ?, ?, ?)";

	private static final String UPDATE_COMPUTER = "UPDATE computer "
			+ "SET name = ?, introduced = ?, discontinued = ?, company_id = ? "
			+ "WHERE id = ?";

	private static final String DELETE_COMPUTER = "DELETE FROM computer "
			+ "WHERE computer.id = :id";

	private static final String DELETE_COMPANY = "DELETE FROM company "
			+ "WHERE company.id = ?";

	private static final String DELETE_COMPUTER_BY_COMPANY = "DELETE FROM computer "
			+ "WHERE company_id = ?";

	private static final String GET_MAX = "SELECT MAX(computer.id) as max FROM computer";

	private static final String SERACH_NAME = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, company.id, company.name "
			+ "FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "WHERE company.name LIKE :computer_name OR computer.name LIKE :company_name " + "ORDER BY computer.name, company.name "
			+ "LIMIT :offset, :limit";

	private static final String SERACH_TOTAL_COMPUTERS = "SELECT COUNT(computer.id) "
			+ "FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "WHERE company.name LIKE :company_name OR computer.name LIKE :computer_name ";

	private static final int NO_CONNECTION = -1;
	private static final boolean IS_UPDATE = true;
	private static final boolean IS_INSERT = false;

	@Autowired
	private ConnectionToDb connectionToDb;

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<ComputerEntity> getComputers() {
		RowMapper<ComputerEntity> vRowMapper = new ComputerRowMapper();
        List<ComputerEntity> computers = jdbcTemplate.query(GET_ALL_COMPUTERS, vRowMapper);
        return computers;
	}
	
	public List<ComputerEntity> getComputersWithPage(Pageable page) {
		StringBuilder query = new StringBuilder();
		query.append(GET_ALL_COMPUTERS_WITH_PAGE)
			 .append(ORDER_BY)
			 .append(LIMIT);
		return getComputersWithPaginate(page, query.toString());
	}

	public List<ComputerEntity> getComputersWithPageAndSort(Pageable page, SortDao sort) {
		StringBuilder query = new StringBuilder();
		query.append(GET_ALL_COMPUTERS_WITH_PAGE)
			 .append(getSortQuery(sort))
			 .append(LIMIT);
		return getComputersWithPaginate(page, query.toString());
	}

	private  MapSqlParameterSource addOffsetAndLimit(Pageable page) {
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		int offset = (page.getNumber() - 1) * page.getSize();
    	int limit = page.getSize();
    	parameterSource.addValue("offset", offset);
    	parameterSource.addValue("limit", limit);
    	return parameterSource;
    }

	private List<ComputerEntity> getComputersWithPaginate(Pageable page, String query) {
		MapSqlParameterSource parameterSource = addOffsetAndLimit(page);
		
		RowMapper<ComputerEntity> computerRowMapper = new ComputerRowMapper();
        List<ComputerEntity> computers = jdbcTemplate.query(query, parameterSource, computerRowMapper);
        return computers;
	}
	
	public List<ComputerEntity> getSerchComputersWithPage(Pageable page, String name) {
		MapSqlParameterSource parameterSource = addNameParameter(name);
		parameterSource.addValues(addOffsetAndLimit(page).getValues());
		RowMapper<ComputerEntity> computerRowMapper = new ComputerRowMapper();
        List<ComputerEntity> computers = jdbcTemplate.query(SERACH_NAME, parameterSource, computerRowMapper);
        return computers;
	}

	private MapSqlParameterSource addNameParameter(String name) {
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		String nameRequest = "%" + name.trim() + "%";
		parameterSource.addValue("computer_name", nameRequest);
		parameterSource.addValue("company_name", nameRequest);
		return parameterSource;
	}

	public long totalOfelements() {
		return aggregatedOperation(GET_TOTAL_PAGES);
	}

	public Optional<ComputerEntity> getComputerById(long id) {
		SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
		RowMapper<ComputerEntity> computerRowMapper = new ComputerRowMapper();
        ComputerEntity computer = (ComputerEntity) jdbcTemplate.query(GET_COMPUTER_BY_ID, parameterSource, computerRowMapper);
        return Optional.ofNullable(computer);
	}

	public int addComputer(ComputerEntity computer) {
		return addComputerOrUpdateIt(computer, IS_INSERT);
	}

	public int updateComputer(ComputerEntity computer) {
		return addComputerOrUpdateIt(computer, IS_UPDATE);
	}

	public int deleteComputerById(long id) {
		SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
		return jdbcTemplate.update(DELETE_COMPUTER, parameterSource);
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
		preparedStatement = connection.prepareStatement(query);
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
			connection.get().commit();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnectionAndStetement(connection.get(), preparedStatement);
		}
		return executeUpdate;
	}

	private int preparStatementAndExecuteUpdate(ComputerEntity computer, Connection connection, boolean isUpdate)
			throws SQLException {
		int index = 1;
		String query = INSERT_COMPUTER;
		if (isUpdate) {
			index = 0;
			query = UPDATE_COMPUTER;
		}
		LOGGER.info("query : {}", query);
		PreparedStatement preparedStatement = connection.prepareStatement(query);
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
		return jdbcTemplate.queryForObject(query, (SqlParameterSource) null, Long.class);
	}

	public long totalComputersFounded(String name) {
		MapSqlParameterSource parameterSource = addNameParameter(name);
		return jdbcTemplate.queryForObject(SERACH_TOTAL_COMPUTERS, parameterSource, Long.class);
	}

	private String getSortQuery(SortDao sort) {
		StringBuilder orderBy = new StringBuilder();
		orderBy.append(sort.getProperty()).append(".name").append(" ").append(sort.getOrder().getSort()).append(" ");
		return orderBy.toString();
	}

}