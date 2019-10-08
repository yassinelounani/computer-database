package dao;

import models.Company;
import models.Computer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CompanyDao implements Dao {

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
    	try {
    		connexion = connexionToDb.getConnectionDb();
    		List<Company> companies = new ArrayList<>();
	    	String query = "SELECT * FROM company;";
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
		}
    	return null;
    }
    
    public Company getCompanyByID(long id) throws SQLException {
    	Connection connexion = null;
    	Company company = null;
    	try {
    		connexion = connexionToDb.getConnectionDb();
	    	String query = "SELECT * FROM company WHERE company.id= ?;";
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
		}
    	return null;
    }


}
