package fr.excilys.cdb.business;

import static fr.excilys.cdb.business.Helper.isValidBean;
import static fr.excilys.cdb.business.Helper.mapAll;
import static fr.excilys.cdb.business.Helper.mapToComputer;
import static fr.excilys.cdb.business.Helper.mapToComputerEntity;
import static fr.excilys.cdb.business.Helper.mapToSortDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.excilys.cdb.api.ComputerService;
import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.api.dto.Identifier;
import fr.excilys.cdb.api.dto.NameAndPage;
import fr.excilys.cdb.api.dto.Page;
import fr.excilys.cdb.api.dto.PageAndSort;
import fr.excilys.cdb.api.exception.NotFoundCompanyException;
import fr.excilys.cdb.api.exception.NotFoundComputerException;
import fr.excilys.cdb.persistence.dao.CompanyDao;
import fr.excilys.cdb.persistence.dao.ComputerDao;
import fr.excilys.cdb.persistence.models.CompanyEntity;
import fr.excilys.cdb.persistence.models.ComputerEntity;
import fr.excilys.cdb.persistence.models.Pageable;
import fr.excilys.cdb.persistence.models.SortDao;

@Service
public class ComputerServiceExporter implements ComputerService {

	private static final int PAGE_1 = 1;

	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerServiceExporter.class);
	
	@Autowired
	private ComputerDao computerDao;

	@Autowired
	private CompanyDao companyDao;
	
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
	
	public List<Computer> getComputersWithPageAndSort(PageAndSort pageAndSort) {
		if (isValidBean(pageAndSort)) {
			Pageable pageable = new Pageable(pageAndSort.getPage().getNumber(), pageAndSort.getPage().getSize());
			SortDao sortDao = mapToSortDao(pageAndSort.getSort());
			List<ComputerEntity> computers = computerDao.getComputersWithPageAndSort(pageable, sortDao);
			LOGGER.info("get all Computers with page {} from Dao Computer", pageAndSort);
			return mapAll(computers);
		}
		return new ArrayList<>();
	}
	
	public List<Computer> getSerchComputersWithPage(NameAndPage nameAndPAge) {
		if (isValidBean(nameAndPAge)) {
			Pageable pageable = new Pageable(nameAndPAge.getPage().getNumber(), nameAndPAge.getPage().getSize());
			List<ComputerEntity> computers = computerDao.getSerchComputersWithPage(pageable, nameAndPAge.getName());
			LOGGER.info("get all Computers Serched with page {} from Dao Computer", nameAndPAge.getPage().getNumber());
			return mapAll(computers);
		}
		return new ArrayList<>();
	}

	public int getTotalPagesOfComputers(Page page) {
		int totalPages = 0;
		if (isValidBean(page)) {
			long totalElements = computerDao.totalOfelements();
			LOGGER.info("get total of elements : {}", totalElements);
			return getTotalPages(page, totalElements);
		}
		return totalPages;
	}

	public int getTotalPagesOfSerchedComputers(NameAndPage nameAndPage) {
		int totalPages = 0;
		if (isValidBean(nameAndPage)) {
			long totalElements = computerDao.totalComputersFounded(nameAndPage.getName());
			LOGGER.info("get total of elements : {}", totalElements);
			return getTotalPages(nameAndPage.getPage(), totalElements);
		}
		return totalPages;
	}

	private int getTotalPages(Page page, long totalElements) {
		int totalPages = (int) (totalElements / page.getSize());
		if(totalElements <9) {
			return PAGE_1;
		}
		totalPages = totalPages % page.getSize() == 0 ? totalPages : totalPages + 1;
		LOGGER.info("get total of pages : {}", totalPages);
		return totalPages;
	}

	public Optional<Computer> getComputerById(Identifier computerId) {
		if (isValidBean(computerId)) {
			Optional<ComputerEntity> computerEntity = computerDao.getComputerById(computerId.getId());
			LOGGER.info("get Computer with id : {} from Dao Computer", computerId.getId());
			Computer computer =  mapToComputer(computerEntity.get());
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

	public int deleteComputerById(Identifier computerId) throws NotFoundComputerException {
		int deleteValue = 0;
		if (isValidBean(computerId)) {
			Optional<Computer> getComputer = getComputerById(computerId);
			if (!getComputer.isPresent()) {
				throw new NotFoundComputerException("Compuetr with id :"+ computerId.getId() +" not Exist");
			}
			deleteValue = computerDao.deleteComputerById(computerId.getId());
		}
		return deleteValue;
	}

	public int deleteCompany(Identifier idcompany) throws NotFoundCompanyException {
		int deleteValue = 0;
		if (isValidBean(idcompany)) {
			Optional<CompanyEntity> getComputer = companyDao.getCompanyById(idcompany.getId());
			if (!getComputer.isPresent()) {
				throw new NotFoundCompanyException("Company with id :"+ idcompany.getId() +" not Exist");
			}
			deleteValue = computerDao.deleteCompany(idcompany.getId());
		}
		return deleteValue;
	}

	public int updateComputer(Computer computer) throws NotFoundComputerException {
		int updateValue = 0;
		if ( isValidBean(computer)) {
			Identifier computerId = new Identifier(computer.getId());
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
