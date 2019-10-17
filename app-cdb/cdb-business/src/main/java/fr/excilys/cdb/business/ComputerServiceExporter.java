package fr.excilys.cdb.business;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.excilys.cdb.api.ComputerService;
import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.api.dto.Page;
import fr.excilys.cdb.api.exception.NotFoundCompanyException;
import fr.excilys.cdb.api.exception.NotFoundComputerException;
import fr.excilys.cdb.persistence.dao.CompanyDao;
import fr.excilys.cdb.persistence.dao.ComputerDao;
import fr.excilys.cdb.persistence.mappers.Mapper;
import fr.excilys.cdb.persistence.models.CompanyEntity;
import fr.excilys.cdb.persistence.models.ComputerEntity;
import fr.excilys.cdb.persistence.models.Pageable;



public class ComputerServiceExporter implements ComputerService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerServiceExporter.class);
	
	private ComputerDao computerDao;
	private CompanyDao companyDao;
	
	
	private static ComputerServiceExporter INSTANCE = null;

    public static synchronized ComputerServiceExporter getInstance(ComputerDao computerDao, CompanyDao companyDao) {
        if (INSTANCE == null) {
            INSTANCE = new ComputerServiceExporter(computerDao, companyDao);
        }
        return INSTANCE;
    }
    
	
	
	private ComputerServiceExporter(ComputerDao computerDao, CompanyDao companyDao) {
		super();
		this.computerDao = computerDao;
		this.companyDao = companyDao;
	}
	
	public List<Computer> getComputers() {
		List<ComputerEntity> computers = computerDao.getComputers();
		LOGGER.info("get all Computers from Dao Computer");
		return Mapper.mapAll(computers, Computer.class);
	}
	
	public List<Computer> getComputersWithPage(Page page) {
		Pageable pageable = Mapper.map(page, Pageable.class);
		List<ComputerEntity> computers = computerDao.getComputersWithPage(pageable);
		LOGGER.info("get all Computers with page {} from Dao Computer", page.getNumber());
		return Mapper.mapAll(computers, Computer.class);
	}
	
	public Optional<Computer> getComputerById(long id) {
		Optional<ComputerEntity> computerEntity = computerDao.getComputerById(id);
		LOGGER.info("get Computer with id : {} from Dao Computer", id);
		Computer computer =  Mapper.map(computerEntity, Computer.class);
		return Optional.ofNullable(computer);
	}
	
	public int addComputer(Computer computer) throws NotFoundCompanyException {
		String id = String.valueOf(computer.getId());
		ComputerEntity computerEntity = Mapper.map(computer, ComputerEntity.class);
		if(computer.getCompany().getId() > 0) {
			Optional<CompanyEntity> company = companyDao.getCompanyById(computer.getCompany().getId());
			if(!company.isPresent()) {
					throw new NotFoundCompanyException("Company with id :"+ id+" not Exist referenced by company_id");
			} else {
				return computerDao.addComputer(computerEntity);
			}
		}
		else {
			return computerDao.addComputer(computerEntity);
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
		computer = prepareComputerToUpdate(computer, getComputer.get());
		ComputerEntity entity = Mapper.map(computer, ComputerEntity.class);
		return computerDao.updateComputer(entity);	
	}
	
	private Computer prepareComputerToUpdate(Computer computer, Computer getComputer) {
		if(computer.getName().isEmpty()) {
			computer.setName(getComputer.getName());
		}
		if(computer.getDateIntroduced() == null) {
			computer.setDateIntroduced(getComputer.getDateIntroduced());
		}
		if(computer.getDateDiscontinued() == null) {
			computer.setDateDiscontinued(getComputer.getDateDiscontinued());	
		}
		if(computer.getCompany().getId() <= 0) {
			computer.setCompany(getComputer.getCompany());
		}
		return computer;
	}

}
