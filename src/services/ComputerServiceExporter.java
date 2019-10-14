package services;

import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

import api.ComputerService;
import api.Page;
import dao.CompanyDao;
import dao.ComputerDao;
import exception.NotFoundCompanyException;
import exception.NotFoundComputerException;
import models.Company;
import models.Computer;

public class ComputerServiceExporter implements ComputerService {
	
	private static final Logger LOGGER = Logger.getLogger(ComputerServiceExporter.class);
	
	private ComputerDao computerDao;
	private CompanyDao companyDao;

	public ComputerServiceExporter(ComputerDao computerDao, CompanyDao companyDao) {
		super();
		this.computerDao = computerDao;
		this.companyDao = companyDao;
	}
	
	public List<Computer> getComputers() {
		List<Computer> computers = computerDao.getComputers();
		LOGGER.info("get all Computers from Dao Computer");
		return computers; 
	}
	
	public List<Computer> getComputersWithPage(Page page) {
		List<Computer> computers = computerDao.getComputersWithPage(page);
		LOGGER.info("get all Computers with page " + page.getNumber() + "from Dao Computer");
		return computers;
	}
	
	public Optional<Computer> getComputerById(long id) {
		Optional<Computer> computer = computerDao.getComputerById(id);
		LOGGER.info("get Computer with id : " + id + " from Dao Computer");
		return computer;
	}
	
	public int addComputer(Computer computer) throws NotFoundCompanyException {
		String id = String.valueOf(computer.getId());
		if(computer.getCompany().getId() > 0) {
			Optional<Company> company = companyDao.getCompanyById(computer.getCompany().getId());
			if(!company.isPresent()) {
					throw new NotFoundCompanyException("Company with id :"+ id+" not Exist referenced by company_id");
			} else {
				return computerDao.addComputer(computer);
			}
		}
		else {
			return computerDao.addComputer(computer);
		}		
	}
	
	public int deleteComputerById(long id) throws NotFoundComputerException {
		Optional<Computer> getComputer = getComputerById(id);
		if(!getComputer.isPresent()) {
			throw new NotFoundComputerException("Company with id :"+ id +" not Exist");
		}
		return computerDao.deleteComputerById(id);
	}
	
	public int updateComputer(Computer computer) throws NotFoundComputerException {
			Optional<Computer> getComputer = getComputerById(computer.getId());
			if(!getComputer.isPresent()) {
				throw new NotFoundComputerException("Company with id :"+ computer.getId() +" not Exist");
			}
			if(computer.getName().isEmpty()) {
				computer.setName(getComputer.get().getName());
			}
			if(computer.getDateIntroduced() == null) {
				computer.setDateIntroduced(getComputer.get().getDateIntroduced());
			}
			if(computer.getDateDiscontinued() == null) {
				computer.setDateDiscontinued(getComputer.get().getDateDiscontinued());	
			}
			if(computer.getCompany().getId() <= 0) {
				computer.setCompany(getComputer.get().getCompany());
			}
			return computerDao.updateComputer(computer);	
	}

}
