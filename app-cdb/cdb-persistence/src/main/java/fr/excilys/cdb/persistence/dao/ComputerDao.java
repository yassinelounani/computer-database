package fr.excilys.cdb.persistence.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import fr.excilys.cdb.persistence.mappers.ComputerRowMapper;
import fr.excilys.cdb.persistence.models.ComputerEntity;
import fr.excilys.cdb.persistence.models.Pageable;
import fr.excilys.cdb.persistence.models.SortDao;
import static fr.excilys.cdb.persistence.mappers.Mapper.addOffsetAndLimit;

public class ComputerDao {

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
			+ "WHERE computer.id=:id";

	private static final String INSERT_COMPUTER = "INSERT INTO computer(id, name, introduced, discontinued, company_id) "
			+ "VALUES(:id, :name, :introduced, :discontinued, :company.id)";

	private static final String UPDATE_COMPUTER = "UPDATE computer "
			+ "SET name = :name, introduced = :introduced, discontinued = :discontinued, company_id = :company.id "
			+ "WHERE id = :id";

	private static final String DELETE_COMPUTER = "DELETE FROM computer "
			+ "WHERE computer.id = :id";

	private static final String DELETE_COMPANY = "DELETE FROM company "
			+ "WHERE company.id = :id";

	private static final String DELETE_COMPUTER_BY_COMPANY = "DELETE FROM computer "
			+ "WHERE company_id = :id";

	private static final String GET_MAX = "SELECT MAX(computer.id) as max FROM computer";

	private static final String SERACH_NAME = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, company.id, company.name "
			+ "FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "WHERE company.name LIKE :computer_name OR computer.name LIKE :company_name " + "ORDER BY computer.name, company.name "
			+ "LIMIT :offset, :limit";

	private static final String SERACH_TOTAL_COMPUTERS = "SELECT COUNT(computer.id) "
			+ "FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "WHERE company.name LIKE :company_name OR computer.name LIKE :computer_name ";


	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<ComputerEntity> getComputers() {
		RowMapper<ComputerEntity> computerRowMapper = new ComputerRowMapper();
        List<ComputerEntity> computers = jdbcTemplate.query(GET_ALL_COMPUTERS, computerRowMapper);
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
        List<ComputerEntity> computers = jdbcTemplate.query(
        		SERACH_NAME,
        		parameterSource,
        		computerRowMapper);
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
        ComputerEntity computer = jdbcTemplate.queryForObject(
				GET_COMPUTER_BY_ID,
				parameterSource,
				computerRowMapper);
        return Optional.ofNullable(computer);
	}

	public int addComputer(ComputerEntity computer) {
		return jdbcTemplate.update(INSERT_COMPUTER, new BeanPropertySqlParameterSource(computer));
	}

	public int updateComputer(ComputerEntity computer) {
		return jdbcTemplate.update(UPDATE_COMPUTER, new BeanPropertySqlParameterSource(computer));
	}

	public int deleteComputerById(long id) {
		SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
		return jdbcTemplate.update(DELETE_COMPUTER, parameterSource);
	}

	@Transactional
	public int deleteCompany(long companyId) {
		SqlParameterSource parameterSource = new MapSqlParameterSource("id", companyId);
		jdbcTemplate.update(DELETE_COMPUTER_BY_COMPANY, parameterSource);
		return jdbcTemplate.update(DELETE_COMPANY, parameterSource);
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
		orderBy.append(sort.getProperty())
			   .append(".name")
			   .append(" ")
			   .append(sort.getOrder()).append(" ");
		return orderBy.toString();
	}

}