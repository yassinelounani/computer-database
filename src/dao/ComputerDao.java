package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import api.Page;
import exception.BadNumberPageException;
import models.Company;
import models.Computer;
import wrappers.HelperDate;

public class ComputerDao implements Dao {
	
	private static final Logger LOGGER = Logger.getLogger(ComputerDao.class);
	
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
    		LOGGER.info("connection well-established to the database ...................");
	    	final String query = "SELECT * FROM computer INNER JOIN company ON computer.company_id = company.id;";
	    	LOGGER.info("query : "+ query);
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
			LOGGER.info("Connexion Closed successfly...............!");	
		}
    	return computers;
    }
    
    public List<Computer> getComputersWithPage(Page page) throws SQLException, BadNumberPageException {
    	List<Computer> computers = new ArrayList<>();
    	Connection connexion = null;
    	try {
    		connexion = connexionToDb.getConnectionDb();
    		LOGGER.info("connection well-established to the database ...................");
    		
    		int totalComputers = getCountComputers();
    		int numPage = totalComputers / page.getSize();
			int numberPagePossible = totalComputers % page.getSize() == 0 ? numPage : numPage + 1;  
			if(page.getNumber() > numberPagePossible ) {
				throw new BadNumberPageException("Number of page is More than exist in base");
			}
    		
	    	final String query = "SELECT * FROM computer INNER JOIN company ON computer.company_id = company.id LIMIT ?, ?";
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
	    		LocalDate introduced = results.getObject("introduced", LocalDate.class);
	    		LocalDate discontinued = results.getObject("discontinued", LocalDate.class);
	    		long idCompany = results.getLong("company_id");
	    		String nameCompany = results.getString("name");
	    		Company company = new Company(idCompany, nameCompany);
	    		computers.add(new Computer(id, name, introduced, discontinued, company));
	    	}
	    	LOGGER.info("List computer is successfly loaded");
				
		} catch (ClassNotFoundException e) {
			e.getMessage();
		} finally {
			connexion.close();
			LOGGER.info("Connexion Closed successfly...............!");	
		}
    	return computers;
    }
    
    private int getCountComputers() throws SQLException {
    	Connection connexion = null;
    	int total = 0;
    	try {
    		connexion = connexionToDb.getConnectionDb();
    		LOGGER.info("connection well-established to the database ...................");
	    	final String query = "SELECT COUNT(id) as total FROM computer";
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

    public Computer getComputerById(long id) throws SQLException {
    	Connection connexion = null;
    	Computer computer = null;
    	try {
    		connexion = connexionToDb.getConnectionDb();
    		LOGGER.info("connection well-established to the database ...................");
	    	final String query = "SELECT * FROM computer INNER JOIN company ON computer.company_id = company.id WHERE computer.id=?;";
	    	LOGGER.info("query : "+ query);
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
			LOGGER.info("Connexion Closed successfly...............!");
		}
    	return null;

    }

    public int addComputer(Computer computer) throws SQLException {
    	Connection connexion = null;
    	try {
    		connexion = connexionToDb.getConnectionDb();
    		LOGGER.info("connection well-established to the database ...................");
	    	final String query = "INSERT INTO computer(id, name, introduced, discontinued, company_id) VALUES(?, ?, ?, ?, ?);";
	    	LOGGER.info("query : "+ query);
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
			LOGGER.info("Connexion Closed successfly...............!");
		}
    	return -1;

    }

    public int deleteComputerById(long id) throws SQLException {
    	Connection connexion = null;
    	try {
    		connexion = connexionToDb.getConnectionDb();
    		LOGGER.info("connection well-established to the database ...................");
	    	final String query = "DELETE FROM computer WHERE computer.id =?";
	    	LOGGER.info("query : "+ query);
	    	PreparedStatement preparedStatement  = connexion.prepareStatement(query);
	    	preparedStatement.setLong(1, id);
	    	
	    	return preparedStatement.executeUpdate();
    	} catch (ClassNotFoundException e) {
			e.getMessage();
		} finally {
			connexion.close();
			LOGGER.info("Connexion Closed successfly...............!");
		}
    	return -1;	
    }

    public int updateComputer(Computer computer) throws SQLException {
        Connection connexion = null;
    	try {
    		connexion = connexionToDb.getConnectionDb();
    		LOGGER.info("connection well-established to the database ...................");
	    	final String query = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?";
	    	LOGGER.info("query : "+ query);
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
			LOGGER.info("Connexion Closed successfly...............!");
		}
    	return -1;	
    }


}
