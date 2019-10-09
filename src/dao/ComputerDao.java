package dao;

import models.Company;
import models.Computer;
import wrappers.HelperDate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import exception.DateBeforeDiscontinuedException;

public class ComputerDao implements Dao {

	ConnectionToDb connexionToDb;
    
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

    public List<Computer> getComputers() throws SQLException {
    	List<Computer> computers = new ArrayList<>();
    	Connection connexion = null;
    	try {
    		connexion = connexionToDb.getConnectionDb();
	    	String query = "SELECT * FROM computer INNER JOIN company ON computer.company_id = company.id;";
	    	Statement statement = connexion.createStatement();
	    	ResultSet results = statement.executeQuery(query);
	    	while (results.next()) { 
	    		long id = results.getLong ("id");
	    		String name = results.getString ("name");
	    		LocalDate introduced = results.getObject("introduced", LocalDate.class);
	    		LocalDate discontinued = results.getObject("discontinued", LocalDate.class);
	    		long idCompany = results.getLong("company_id");
	    		String nameCompany = results.getString("name");
	    		Company company = new Company(idCompany, nameCompany);
	    		computers.add(new Computer(id, name, introduced, discontinued, company));
	    	}
				
		} catch (ClassNotFoundException e) {
			e.getMessage();
		} finally {
			connexion.close();
		}
    	return computers;
    }

    public Computer getComputerById(long id) throws SQLException {
    	Connection connexion = null;
    	Computer computer = null;
    	try {
    		connexion = connexionToDb.getConnectionDb();
	    	String query = "SELECT * FROM computer INNER JOIN company ON computer.company_id = company.id WHERE computer.id=?;";
	    	PreparedStatement preparedStatement  = connexion.prepareStatement(query);
	    	preparedStatement.setLong(1, id);
	    	ResultSet results = preparedStatement.executeQuery();
	    	while (results.next()) { 
		    	long idComputer = results.getLong("id");
		    	String name = results.getString("name");
		    	LocalDate dateIntroduced = results.getObject("introduced", LocalDate.class);
		    	LocalDate dateDiscontinued = results.getObject("discontinued", LocalDate.class);
		    	long idCompany = results.getLong("company_id");
	    		String nameCompany = results.getString("name");
	    		Company company = new Company(idCompany, nameCompany);
    		
	    		computer =  new Computer(idComputer, name, dateIntroduced, dateDiscontinued, company);
	    	}
	    	return computer;
	    	
		} catch (ClassNotFoundException e) {
			e.getMessage();
		} finally {
			connexion.close();
		}
    	return null;

    }

    public int addComputer(Computer computer) throws SQLException {
    	Connection connexion = null;
    	try {
    		connexion = connexionToDb.getConnectionDb();
    		
	    	String query = "INSERT INTO computer(id, name, introduced, discontinued, company_id) VALUES(?, ?, ?, ?, ?);";
	    	PreparedStatement preparedStatement  = connexion.prepareStatement(query);
	    	preparedStatement.setLong(1, computer.getId());
	    	preparedStatement.setString(2, computer.getName());
	    	preparedStatement.setDate(3, HelperDate.dateToSql(computer.getDateIntroduced()));
	    	preparedStatement.setDate(4, HelperDate.dateToSql(computer.getDateDiscontinued()));
	    	if(computer.getCompany().getId() > 0 ) {
	    		preparedStatement.setLong(5, computer.getCompany().getId());
	    	} else {
	    		preparedStatement.setNull(5, Types.DATE);
	    	}
	    	
	    	return preparedStatement.executeUpdate();
	    	
		} catch (ClassNotFoundException e) {
			e.getMessage();
		} finally {
			connexion.close();
		}
    	return -1;

    }

    public int deleteComputerById(long id) throws SQLException {
    	Connection connexion = null;
    	try {
    		connexion = connexionToDb.getConnectionDb();
    		
	    	String query = "DELETE FROM computer WHERE computer.id =?";
	    	PreparedStatement preparedStatement  = connexion.prepareStatement(query);
	    	preparedStatement.setLong(1, id);
	    	
	    	return preparedStatement.executeUpdate();
    	} catch (ClassNotFoundException e) {
			e.getMessage();
		} finally {
			connexion.close();
		}
    	return -1;	
    }

    public int updateComputer(Computer computer) throws SQLException {
        Connection connexion = null;
    	try {
    		connexion = connexionToDb.getConnectionDb();
    		
	    	String query = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?";
	    	PreparedStatement preparedStatement  = connexion.prepareStatement(query);
	    	preparedStatement.setString(1, computer.getName());
	    	preparedStatement.setDate(2, HelperDate.dateToSql(computer.getDateIntroduced()));
	    	preparedStatement.setDate(3, HelperDate.dateToSql(computer.getDateDiscontinued()));
	    	preparedStatement.setLong(4, computer.getCompany().getId());
	    	preparedStatement.setLong(5, computer.getId());
	    	
	    	return preparedStatement.executeUpdate();
    	} catch (ClassNotFoundException e) {
			e.getMessage();
		} finally {
			connexion.close();
		}
    	return -1;	
    }


}
