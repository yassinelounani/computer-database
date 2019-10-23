package fr.excilys.cdb.business;

import static fr.excilys.cdb.business.Helper.isValidBean;
import static fr.excilys.cdb.business.Helper.mapAll;
import static fr.excilys.cdb.business.Helper.mapToComputerEntity;
import static fr.excilys.cdb.business.Helper.mapToComputer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.excilys.cdb.api.ComputerService;
import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.api.dto.ComputerId;
import fr.excilys.cdb.api.dto.Page;
import fr.excilys.cdb.api.exception.NotFoundCompanyException;
import fr.excilys.cdb.api.exception.NotFoundComputerException;
import fr.excilys.cdb.persistence.dao.CompanyDao;
import fr.excilys.cdb.persistence.dao.ComputerDao;
import fr.excilys.cdb.persistence.models.CompanyEntity;
import fr.excilys.cdb.persistence.models.ComputerEntity;
import fr.excilys.cdb.persistence.models.Pageable;

public class ComputerServiceExporter implements ComputerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerServiceExporter.class);

	private ComputerDao computerDao;
	private CompanyDao companyDao;

	static ComputerServiceExporter instance = null;

    public static synchronized ComputerServiceExporter getInstance() {
        if (instance == null) {
            instance = new ComputerServiceExporter();
        }
        return instance;
    }

	private ComputerServiceExporter() {
		super();
		this.computerDao = ComputerDao.getInstance();
		this.companyDao = CompanyDao.getInstance();
	}

	public List<Computer> getComputers() {
		List<ComputerEntity> computers = computerDao.getComputers();
		LOGGER.info("get all Computers from Dao Computer");
		return mapAll(computers);
	}

	public List<Computer> getComputersWithPage(Page page) {
		if (isValidBean(page)) {
			Pageable pageable = new Pageable(page.getNumber(), page.getSize());
			List<ComputerEntity> computers = computerDao.getComputersWithPage(pageable);
			LOGGER.info("get all Computers with page {} from Dao Computer", page.getNumber());
			return mapAll(computers);
		}
		return new ArrayList<>();
	}

	public int getTotalOfPages(Page page) {
		int totalPages = 0;
		if (isValidBean(page)) {
			long totalElements = computerDao.totalOfelements();
			LOGGER.info("get total of elements : {}", totalElements);
			totalPages = (int) (totalElements / page.getSize());
			totalPages = totalPages % page.getSize() == 0 ? totalPages : totalPages + 1;
			LOGGER.info("get total of pages : {}", totalPages);
			return totalPages;
		}
		return totalPages;
	}

	public Optional<Computer> getComputerById(ComputerId computerId) {
		if (isValidBean(computerId)) {
			Optional<ComputerEntity> computerEntity = computerDao.getComputerById(computerId.getId());
			LOGGER.info("get Computer with id : {} from Dao Computer", computerId.getId());
			Computer computer =  mapToComputer(computerEntity.get());
			System.err.println(computer);
			return Optional.of(computer);
		}
		return Optional.empty();
	}

	public int addComputer(Computer computer) throws NotFoundCompanyException {
		int addValue = 0;
		if (isValidBean(computer)) {
			LOGGER.info("Valid bean Computer .................................................");
			String id = String.valueOf(computer.getId());
			ComputerEntity computerEntity = mapToComputerEntity(computer);
			if(computer.getIdCompany() != 0) {
				Optional<CompanyEntity> company = companyDao.getCompanyById(computer.getIdCompany());
				if (!company.isPresent()) {
						throw new NotFoundCompanyException("Company with id :"+ id+" not Exist referenced by company_id");
				}
			}
			long idcomputer = computerDao.getMaxIdComputer() + 1;
			computerEntity.setId(idcomputer);
			addValue = computerDao.addComputer(computerEntity);
		}
		return addValue;
	}

	public int deleteComputerById(ComputerId computerId) throws NotFoundComputerException {
		int deleteValue = 0;
		if (isValidBean(computerId)) {
			Optional<Computer> getComputer = getComputerById(computerId);
			if (getComputer.isPresent()) {
				throw new NotFoundComputerException("Company with id :"+ computerId.getId() +" not Exist");
			}
			deleteValue = computerDao.deleteComputerById(computerId.getId());
		}
		return deleteValue;
	}

	public int updateComputer(Computer computer) throws NotFoundComputerException {
		int updateValue = 0;
		if ( isValidBean(computer)) {
			ComputerId computerId = new ComputerId(computer.getId());
			Optional<Computer> getComputer = getComputerById(computerId);
			if (!getComputer.isPresent()) {
				throw new NotFoundComputerException("Company with id :"+ computer.getId() +" not Exist");
			}
			computer = prepareComputerToUpdate(computer, getComputer.get());
			ComputerEntity entity = mapToComputerEntity(computer);
			updateValue = computerDao.updateComputer(entity);
		}
		return updateValue;
	}

	private Computer prepareComputerToUpdate(Computer computer, Computer getComputer) {
		if (computer.getName().isEmpty()) {
			computer.setName(getComputer.getName());
		}
		if (computer.getIntroduced() == null) {
			computer.setIntroduced(getComputer.getIntroduced());
		}
		if (computer.getDiscontinued() == null) {
			computer.setDiscontinued(getComputer.getDiscontinued());	
		}
		if (computer.getIdCompany() <= 0) {
			computer.setIdCompany(computer.getIdCompany());
		}
		return computer;
	}
}
