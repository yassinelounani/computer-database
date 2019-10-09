package services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import api.ComputerService;
import dao.CompanyDao;
import dao.ComputerDao;
import exception.DateBeforeDiscontinuedException;
import exception.NotFoundCompanyException;
import exception.NotFoundComputerException;
import models.Company;
import models.Computer;

public class ComputerServiceExporter implements ComputerService {
	
	private ComputerDao computerDao;
	private CompanyDao companyDao;

	public ComputerServiceExporter(ComputerDao computerDao, CompanyDao companyDao) {
		super();
		this.computerDao = computerDao;
		this.companyDao = companyDao;
	}
	

	public List<Computer> getComputers() throws NotFoundComputerException {
		List<Computer> computers = new ArrayList<>();
		try {
			computers = computerDao.getComputers();
			if(computers.isEmpty()) {
				throw new NotFoundComputerException("Any Computer found in DataBase");
			}
			return computers;
			
		} catch (SQLException e) {
			e.getMessage();
		}
		return computers; 
	}
	
	public Computer getComputerById(long id) throws NotFoundComputerException {
		if(id <= 0) return null;
		try {
			Computer computer = computerDao.getComputerById(id);
			if(computer == null) {
				throw new NotFoundComputerException("Computer Not Found");
			}
			return computer;
		}
		catch (SQLException e) {
			e.getMessage();
		}
		return null;
	}
	
	public int addComputer(Computer computer) throws NotFoundCompanyException {
		if(computer == null) {
			return -2;
		}
		String id = String.valueOf(computer.getId());
		try {
			if(computer.getCompany().getId() > 0) {
				Company company = companyDao.getCompanyByID(computer.getCompany().getId());
				if(company == null) {
					throw new NotFoundCompanyException("Company with id :"+ id+" not Exist");
				} else {
					return computerDao.addComputer(computer);
				}
			}
			else {
				return computerDao.addComputer(computer);
			}
			
		} catch( SQLException e) {
			e.getMessage();
		}
		return -1;
			
	}
	
	public int deleteComputerById(long id) {
		if(id <= 0) {
			return -2; 
		}
		try {
			return computerDao.deleteComputerById(id);
		}catch (SQLException e) {
			e.getMessage();
		}
		return -1;
	}
	
	public int updateComputer(Computer computer) throws NotFoundComputerException{
		if(computer == null) {
			return -2;
		}
		try {
			Computer getComputer = getComputerById(computer.getId());
			if(computer.getName().isEmpty()) {
				computer.setName(getComputer.getName());
			}
			if(computer.getDateIntroduced() == null) {
				computer.setDateIntroduced(getComputer.getDateIntroduced());
			}
			if(computer.getDateDiscontinued() == null) {
				try {
					computer.setDateDiscontinued(getComputer.getDateDiscontinued());
				} catch (DateBeforeDiscontinuedException e) {
					e.getMessage();
				}
			}
			if(computer.getCompany().getId() <= 0) {
				computer.setCompany(getComputer.getCompany());
			}
			return computerDao.updateComputer(computer);
		} catch( SQLException e) {
			e.getMessage();
		}
		return -1;
	}

}
