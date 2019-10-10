package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import api.Page;
import exception.BadNumberPageException;
import models.Company;

public class CompanyDao implements Dao {

	private static final Logger LOGGER = Logger.getLogger(CompanyDao.class);
	
	ConnectionToDb connexionToDb;
	
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

    public List<Company> getCompanies() throws SQLException {
    	Connection connexion = null;
    	List<Company> companies = new ArrayList<>();
    	try {
    		connexion = connexionToDb.getConnectionDb();
    		LOGGER.info("connection well-established to the database ...................");
	    	final String  query = "SELECT * FROM company;";
	    	LOGGER.info("query : "+ query);
	    	Statement statement = connexion.createStatement();
	    	ResultSet results = statement.executeQuery(query);
	    	while (results.next()) { 
	    		long id = results.getLong ("id");
	    		String name = results.getString ("name");
	    		companies.add(new Company(id, name));
	    	}
			return companies;	
		} catch (ClassNotFoundException e) {
			e.getMessage();
		} finally {
			connexion.close();
			LOGGER.info("Connexion Closed successfly...............!");
		}
    	return null;
    }
    
    public List<Company> getCompaniesWithPage(Page page) throws SQLException, BadNumberPageException {
    	Connection connexion = null;
    	List<Company> companies = new ArrayList<>();
    	try {
    		connexion = connexionToDb.getConnectionDb();
    		LOGGER.info("connection well-established to the database ...................");
    		
    		int totalComputers = getCountCompanies();
    		int numPage = totalComputers / page.getSize();
			int numberPagePossible = totalComputers % page.getSize() == 0 ? numPage : numPage + 1;  
			if(page.getNumber() > numberPagePossible ) {
				throw new BadNumberPageException("Number of page is More than exist in base");
			}
    		
	    	final String query = "SELECT * FROM company LIMIT ?, ?;";
	    	LOGGER.info("query : "+ query);
	    	PreparedStatement preparedStatement  = connexion.prepareStatement(query);
	    	int offset = (page.getNumber() - 1) * page.getSize();
	    	int limit = page.getSize();
	    	preparedStatement.setInt(1, offset);
	    	preparedStatement.setInt(2, limit);
	    	ResultSet results = preparedStatement.executeQuery();
	    	while (results.next()) { 
	    		long id = results.getLong ("id");
	    		String name = results.getString ("name");
	    		companies.add(new Company(id, name));
	    	}
			return companies;	
		} catch (ClassNotFoundException e) {
			e.getMessage();
		} finally {
			connexion.close();
			LOGGER.info("Connexion Closed successfly...............!");
		}
    	return null;
    }
    
    public Company getCompanyByID(long id) throws SQLException {
    	Connection connexion = null;
    	Company company = null;
    	try {
    		connexion = connexionToDb.getConnectionDb();
    		LOGGER.info("connection well-established to the database ...................");
	    	final String query = "SELECT * FROM company WHERE company.id= ?;";
	    	LOGGER.info("query : "+ query);
	    	PreparedStatement preparedStatement  = connexion.prepareStatement(query);
	    	preparedStatement.setLong(1, id);
	    	ResultSet results = preparedStatement.executeQuery();
	    	while (results.next()) { 
		    	long idCompany = results.getLong("id");
	    		String nameCompany = results.getString("name");
	    	    company = new Company(idCompany, nameCompany);
	    	}
	    	return company;
	    	
		} catch (ClassNotFoundException e) {
			e.getMessage();
		} finally {
			connexion.close();
			LOGGER.info("Connexion Closed successfly...............!");
		}
    	return null;
    }
    
    private int getCountCompanies() throws SQLException {
    	Connection connexion = null;
    	int total = 0;
    	try {
    		connexion = connexionToDb.getConnectionDb();
    		LOGGER.info("connection well-established to the database ...................");
	    	final String query = "SELECT COUNT(id) as total FROM company";
	    	LOGGER.info("query : "+ query);
	    	Statement statement = connexion.createStatement();
	    	ResultSet results = statement.executeQuery(query);
	    	while (results.next()) { 
	    		total = results.getInt("total");	
	    	}
				
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		} finally {
			connexion.close();
			LOGGER.info("Connexion Closed successfly...............!");	
		}
    	return total;
    }


}
