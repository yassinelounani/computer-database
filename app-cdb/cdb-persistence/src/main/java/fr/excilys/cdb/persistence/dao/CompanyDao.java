package fr.excilys.cdb.persistence.dao;

import static fr.excilys.cdb.persistence.mappers.Mapper.addOffsetAndLimit;
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
import fr.excilys.cdb.persistence.mappers.CompanyRowMapper;
import fr.excilys.cdb.persistence.models.CompanyEntity;
import fr.excilys.cdb.persistence.models.Pageable;


public class CompanyDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyDao.class);

	private static final String GET_ALL_COMPANIES = "SELECT company.id, company.name "
												  + "FROM company";

	private static final String GET_COMPANY_BY_ID = "SELECT company.id, company.name " 
												  + "FROM company "
												  + "WHERE company.id= :id";

	public static final String GET_ALL_COMPANIES_WITH_PAGE = "SELECT company.id, company.name "
															+ "FROM company "
															+ "LIMIT :offset, :limit";

	public static final String DELETE_COMPANY_BY_ID = "DELETE "
													+ "FROM company "
													+ "WHERE id = :id";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<CompanyEntity> getCompanies() {
		RowMapper<CompanyEntity> companyRowMapper = new CompanyRowMapper();
        List<CompanyEntity> computers = jdbcTemplate.query(GET_ALL_COMPANIES, companyRowMapper);
        return computers;
	}

	public List<CompanyEntity> getCompaniesWithPage(Pageable page) {
		MapSqlParameterSource parameterSource = addOffsetAndLimit(page);
		
		RowMapper<CompanyEntity> companyRowMapper = new CompanyRowMapper();
        List<CompanyEntity> companies = jdbcTemplate.query(
        		GET_ALL_COMPANIES_WITH_PAGE,
        		parameterSource,
        		companyRowMapper);
        return companies;
	}

	public Optional<CompanyEntity> getCompanyById(long companyId) {
		SqlParameterSource parameterSource = new MapSqlParameterSource("id", companyId);
		RowMapper<CompanyEntity> computerRowMapper = new CompanyRowMapper();
		CompanyEntity company = jdbcTemplate.queryForObject(
				GET_COMPANY_BY_ID,
				parameterSource,
				computerRowMapper);
		return Optional.ofNullable(company);
	}
}
