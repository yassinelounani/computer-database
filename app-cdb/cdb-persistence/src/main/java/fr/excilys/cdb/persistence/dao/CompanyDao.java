package fr.excilys.cdb.persistence.dao;

import static fr.excilys.cdb.persistence.dao.ConnectionToDb.closeConnectionAndStetement;
import static fr.excilys.cdb.persistence.dao.ConnectionToDb.prepareStetementAndExecureQuerytWithPage;
import static fr.excilys.cdb.persistence.mappers.Mapper.ID_COMPANY;
import static fr.excilys.cdb.persistence.mappers.Mapper.mapResultSetToCompany;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import fr.excilys.cdb.persistence.models.CompanyEntity;
import fr.excilys.cdb.persistence.models.Pageable;

@Component
public class CompanyDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyDao.class);
	private static final String GET_ALL_COMPANIES = "SELECT company.id, company.name " + "FROM company";

	private static final String GET_COMPANY_BY_ID = "SELECT company.id, company.name " + "FROM company "
			+ "WHERE company.id= ?";

	public static final String GET_ALL_COMPANIES_WITH_PAGE = "SELECT company.id, company.name " + "FROM company "
			+ "LIMIT ?, ?;";
	public static final String DELETE_COMPANY_BY_ID = "DELETE " + "FROM company " + "WHERE id=?";
	
	@Autowired
	private ConnectionToDb connectionToDb;

	public List<CompanyEntity> getCompanies() {
		Statement statement = null;
		List<CompanyEntity> companies = new ArrayList<>();
		Optional<Connection> connection = connectionToDb.getConnection();
		if (!connection.isPresent()) {
			return companies;
		}
		try {
			statement = connection.get().createStatement();
			LOGGER.info("query : {}", GET_ALL_COMPANIES);
			ResultSet results = statement.executeQuery(GET_ALL_COMPANIES);
			while (results.next()) {
				companies.add(mapResultSetToCompany(results, ID_COMPANY));
			}
			LOGGER.info("List computer is successfly loaded");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			companies.clear();
		} finally {
			closeConnectionAndStetement(connection.get(), statement);
		}
		return companies;
	}

	public List<CompanyEntity> getCompaniesWithPage(Pageable page) {
		List<CompanyEntity> companies = new ArrayList<>();
		PreparedStatement preparedStatement = null;
		Optional<Connection> connection = connectionToDb.getConnection();
		if (!connection.isPresent()) {
			return companies;
		}
		LOGGER.info("connection well-established to the database ...................");
		try {
			LOGGER.info("query : {}", GET_ALL_COMPANIES_WITH_PAGE);
			preparedStatement = connection.get().prepareStatement(GET_ALL_COMPANIES_WITH_PAGE);
			ResultSet results = prepareStetementAndExecureQuerytWithPage(page, preparedStatement);
			while (results.next()) {
				companies.add(mapResultSetToCompany(results, ID_COMPANY));
			}
			LOGGER.info("List computer is successfly loaded");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			companies.clear();
		} finally {
			closeConnectionAndStetement(connection.get(), preparedStatement);
		}
		return companies;
	}

	public Optional<CompanyEntity> getCompanyById(long companyId) {
		CompanyEntity company = null;
		PreparedStatement preparedStatement = null;
		Optional<Connection> connection = connectionToDb.getConnection();
		if (!connection.isPresent())
			return Optional.empty();
		LOGGER.info("connection well-established to the database ...................");
		try {
			LOGGER.info("query : {}", GET_COMPANY_BY_ID);
			preparedStatement = connection.get().prepareStatement(GET_COMPANY_BY_ID);
			preparedStatement.setLong(1, companyId);
			ResultSet results = preparedStatement.executeQuery();
			if (results.first()) {
				company = mapResultSetToCompany(results, ID_COMPANY);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnectionAndStetement(connection.get(), preparedStatement);
		}
		return Optional.ofNullable(company);
	}
}
