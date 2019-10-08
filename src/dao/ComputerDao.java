package dao;

import models.Company;
import models.Computer;
import wrappers.HelperDate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    	
    	Connection connexion = null;
    	try {
    		connexion = connexionToDb.getConnectionDb();
			List<Computer> computers = new ArrayList<>();
	    	String query = "SELECT * FROM computer INNER JOIN company ON computer.company_id = company.id;";
	    	Statement statement = connexion.createStatement();
	    	ResultSet results = statement.executeQuery(query);
	    	while (results.next()) { 
	    		long id = results.getLong ("id");
	    		String name = results.getString ("name");
	    		LocalDate introduced = results.getDate ("introduced").toLocalDate();
	    		LocalDate discontinued = results.getDate("discontinued").toLocalDate();
	    		long idCompany = results.getLong("company_id");
	    		String nameCompany = results.getString("name");
	    		Company company = new Company(idCompany, nameCompany);
	    		computers.add(new Computer(id, name, introduced, discontinued, company));
	    	}
			return computers;	
		} catch (ClassNotFoundException e) {
			e.getMessage();
		} finally {
			connexion.close();
		}
    	return null;
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
		    	LocalDate dateIntroduced = results.getDate("introduced").toLocalDate();
		    	LocalDate dateDiscontinued = results.getDate("discontinued").toLocalDate();
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
	    	preparedStatement.setLong(5, computer.getCompany().getId());
	    	
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
