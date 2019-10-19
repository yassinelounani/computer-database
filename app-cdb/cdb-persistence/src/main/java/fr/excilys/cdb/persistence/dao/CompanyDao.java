package fr.excilys.cdb.persistence.dao;

import static fr.excilys.cdb.persistence.dao.ConnectionToDb.closeConnectionAndStetement;
import static fr.excilys.cdb.persistence.dao.ConnectionToDb.prepareStetementAndExecureQuerytWithPage;
import static fr.excilys.cdb.persistence.mappers.Mapper.ID_COMPANY;

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

import fr.excilys.cdb.persistence.mappers.Mapper;
import fr.excilys.cdb.persistence.models.CompanyEntity;
import fr.excilys.cdb.persistence.models.Pageable;

public class CompanyDao implements Dao {

	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyDao.class);
	private static final String  GET_ALL_COMPANIES = "SELECT company.id, company.name "
												   + "FROM company";

	private static final String GET_COMPANY_BY_ID = "SELECT company.id, company.name "
												  + "FROM company "
												  + "WHERE company.id= ?";

	public static final String GET_ALL_COMPANIES_WITH_PAGE = "SELECT company.id, company.name "
														   + "FROM company "
														   + "LIMIT ?, ?;";

	private ConnectionToDb connectionTodb;

    private CompanyDao() {
        super();
        this.connectionTodb = ConnectionToDb.getInstance();
    }
    private static CompanyDao INSTANCE = null;

    public static synchronized CompanyDao getInstance()
    {
        if (INSTANCE == null) {
            INSTANCE = new CompanyDao();
        }
        return INSTANCE;
    }
    public List<CompanyEntity> getCompanies() {
    	Statement statement = null;
    	List<CompanyEntity> companies = new ArrayList<>();
    	Optional<Connection> connection = connectionTodb.getConnection();
    	if(!connection.isPresent()) return companies;
    	try {
	    		statement = connection.get().createStatement();
		    	LOGGER.info("query : {}", GET_ALL_COMPANIES);
		    	ResultSet results = statement.executeQuery(GET_ALL_COMPANIES);
		    	while (results.next()) {
		    	   companies.add(Mapper.mapResultSetToCompany(results, Mapper.ID_COMPANY)); 			
		    	}	
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
    	Optional<Connection> connection = connectionTodb.getConnection();
    	if(!connection.isPresent()) return companies;
    	LOGGER.info("connection well-established to the database ...................");
    	try {
    			LOGGER.info("query : {}", GET_ALL_COMPANIES_WITH_PAGE );
		    	preparedStatement  = connection.get().prepareStatement(GET_ALL_COMPANIES_WITH_PAGE);
		    	ResultSet results = prepareStetementAndExecureQuerytWithPage(page, preparedStatement);
		    	while (results.next()) { 
		    		companies.add(Mapper.mapResultSetToCompany(results, ID_COMPANY));
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
    public Optional<CompanyEntity> getCompanyById(long id) {
    	CompanyEntity company = null;
    	PreparedStatement preparedStatement = null;
    	Optional<Connection> connection = connectionTodb.getConnection();
    	if(!connection.isPresent()) return Optional.empty();
    	LOGGER.info("connection well-established to the database ...................");
    	try {
		    	LOGGER.info("query : {}",GET_COMPANY_BY_ID);
		    	preparedStatement  = connection.get().prepareStatement(GET_COMPANY_BY_ID);
		    	preparedStatement.setLong(1, id);
		    	ResultSet results = preparedStatement.executeQuery();
		    	while (results.next()) { 
		    		company = Mapper.mapResultSetToCompany(results, ID_COMPANY);
		    	}
		} catch (SQLException e) {
				System.out.println(e.getMessage());
		} finally {
				closeConnectionAndStetement(connection.get(), preparedStatement);
		}
    	return Optional.ofNullable(company);
    }
}
