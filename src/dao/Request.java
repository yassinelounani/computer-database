package dao;

public class Request {
	
	public static final String GET_ALL_COMPUTERS = 
			  "SELECT id, name, introduced, discontinued, company_id, company.id "
			+ "FROM computer INNER JOIN company ON computer.company_id = company.id;";
	
	public static final String GET_ALL_COMPUTERS_WITH_PAGE = 
			  "SELECT id, name, introduced, discontinued, company_id, company.id "
			+ "FROM computer INNER JOIN company ON computer.company_id = company.id "
			+ "LIMIT ?, ?";
	
	public static final String COUNT_COMPUTERS = 
			  "SELECT COUNT(id) as total "
		    + "FROM computer";
	
	public static final String GET_COMPUTER_BY_ID = 
			  "SELECT id, name, introduced, discontinued, company_id, company.id "
			+ "FROM computer INNER JOIN company ON computer.company_id = company.id "
			+ "WHERE computer.id=?;";
	
	public static final String INSERT_COMPUTER = 
			  "INSERT INTO computer(id, name, introduced, discontinued, company_id) "
			+ "VALUES(?, ?, ?, ?, ?);";
	
	public static final String UPDATE_COMPUTER = 
			  "UPDATE computer "
			+ "SET name = ?, introduced = ?, discontinued = ?, company_id = ? "
			+ "WHERE id = ?";
	
	public static final String DELETE_COMPUTER =
			  "DELETE FROM computer "
		    + "WHERE computer.id =?";

}
