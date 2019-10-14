package dao;

import static dao.ConnectionToDb.closeConnectionAndStetement;
import static dao.ConnectionToDb.prepareStetementAndExecureQuerytWithPage;
import static wrappers.Mapper.ID;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

import api.Page;
import models.Company;
import wrappers.Mapper;

public class CompanyDao implements Dao {

	private static final Logger LOGGER = Logger.getLogger(CompanyDao.class);
	
	private static final String  GET_ALL_COMPANIES = "SELECT id, name "
												   + "FROM company";
	
	private static final String GET_COMPANY_BY_ID = "SELECT id, name "
												  + "FROM company "
												  + "WHERE company.id= ?";
	
	public static final String GET_ALL_COMPANIES_WITH_PAGE = "SELECT * FROM company LIMIT ?, ?;";
	
	private ConnectionToDb connexionToDb;
	
    private CompanyDao(ConnectionToDb connexionToDb) {
        super();
        this.connexionToDb = connexionToDb;
    }

    private static CompanyDao INSTANCE = null;

    public static synchronized CompanyDao getInstance(ConnectionToDb connexionToDb)
    {
        if (INSTANCE == null) {
            INSTANCE = new CompanyDao(connexionToDb);
        }
        return INSTANCE;
    }

    public List<Company> getCompanies() {
    	Statement statement = null;
    	List<Company> companies = new ArrayList<>();
    	Optional<Connection>  connection = connexionToDb.getConnectionDb();
    	if(!connection.isPresent()) return companies;
    	try {
	    		statement = connection.get().createStatement();
		    	LOGGER.info("query : " + GET_ALL_COMPANIES);
		    	ResultSet results = statement.executeQuery(GET_ALL_COMPANIES);
		    	while (results.next()) {
		    	   companies.add(Mapper.mapResultSetToCompany(results, Mapper.ID)); 			
		    	}	
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			companies.clear();
		} finally {
			closeConnectionAndStetement(connection.get(), statement);
		}
    	return companies;
    }
      
    public List<Company> getCompaniesWithPage(Page page) {
    	List<Company> companies = new ArrayList<>();
    	Optional<Connection> connection = connexionToDb.getConnectionDb();
    	PreparedStatement preparedStatement = null;
    	if(!connection.isPresent()) return companies;
    	LOGGER.info("connection well-established to the database ...................");
    	try {
    			LOGGER.info("query : " + GET_ALL_COMPANIES_WITH_PAGE );
		    	preparedStatement  = connection.get().prepareStatement(GET_ALL_COMPANIES_WITH_PAGE);
		    	ResultSet results = prepareStetementAndExecureQuerytWithPage(page, preparedStatement);
		    	while (results.next()) { 
		    		companies.add(Mapper.mapResultSetToCompany(results, ID));
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
   
    
    public Optional<Company> getCompanyById(long id) {
    	Company company = null;
    	PreparedStatement preparedStatement = null;
    	Optional<Connection> connection = connexionToDb.getConnectionDb();
    	if(!connection.isPresent()) return Optional.empty();
    	LOGGER.info("connection well-established to the database ...................");
    	try {
		    	LOGGER.info("query : "+ GET_COMPANY_BY_ID);
		    	preparedStatement  = connection.get().prepareStatement(GET_COMPANY_BY_ID);
		    	preparedStatement.setLong(1, id);
		    	ResultSet results = preparedStatement.executeQuery();
		    	while (results.next()) { 
		    		company = Mapper.mapResultSetToCompany(results, ID);
		    	}
		} catch (SQLException e) {
				System.out.println(e.getMessage());
		} finally {
				closeConnectionAndStetement(connection.get(), preparedStatement);
		}
    	return Optional.ofNullable(company);
    }


}
