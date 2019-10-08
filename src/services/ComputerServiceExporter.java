package services;

import java.sql.SQLException;
import java.util.List;

import dao.CompanyDao;
import dao.ComputerDao;
import exception.NotFoundCompanyException;
import exception.NotFoundComputerException;
import models.Company;
import models.Computer;

public class ComputerServiceExporter {
	
	private ComputerDao computerDao;
	private CompanyDao companyDao;

	public ComputerServiceExporter(ComputerDao computerDao, CompanyDao companyDao) {
		super();
		this.computerDao = computerDao;
		this.companyDao = companyDao;
	}
	

	public List<Computer> getComputers() throws SQLException, NotFoundComputerException {
		List<Computer> computers = computerDao.getComputers();
		if(computers.isEmpty()) {
			throw new NotFoundComputerException("Any Computer found in DataBase");
		}
		return computers; 
	}
	
	public Computer getComputerById(long id) throws SQLException, NotFoundComputerException {
		Computer computer = computerDao.getComputerById(id);
		if(computer == null) {
			throw new NotFoundComputerException("Computer Not Found");
		}
		return computer;
	}
	
	public int addComputer(Computer computer) throws SQLException, NotFoundCompanyException {
		String id = String.valueOf(computer.getId());
		Company company = companyDao.getCompanyByID(computer.getCompany().getId());
		if(company == null) {
			throw new NotFoundCompanyException("Company with id :"+ id+" not Exixst");
		} else {
			return computerDao.addComputer(computer);
		}	
	}
	
	
	
	
	
	

}
