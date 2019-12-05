package fr.excilys.cdb.business;

import static fr.excilys.cdb.business.Helper.isValidBean;
import static fr.excilys.cdb.business.Helper.mapAll;
import static fr.excilys.cdb.business.Helper.mapAllComputersWithPage;
import static fr.excilys.cdb.business.Helper.mapToComputer;
import static fr.excilys.cdb.business.Helper.mapToComputerEntity;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.excilys.cdb.api.ComputerService;
import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.api.dto.Identifier;
import fr.excilys.cdb.api.dto.PageDto;
import fr.excilys.cdb.api.dto.SortDto;
import fr.excilys.cdb.api.exception.NotFoundCompanyException;
import fr.excilys.cdb.api.exception.NotFoundComputerException;
import fr.excilys.cdb.persistence.models.CompanyEntity;
import fr.excilys.cdb.persistence.models.ComputerEntity;
import fr.excilys.cdb.persistence.repositories.CompanyRepository;
import fr.excilys.cdb.persistence.repositories.ComputerRepository;

@Service
public class ComputerServiceExporter implements ComputerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerServiceExporter.class);
	
	private ComputerRepository computerRepository;
	private CompanyRepository companyRepository;

	@Autowired
	public ComputerServiceExporter(ComputerRepository computerRepository, CompanyRepository companyRepository) {
		super();
		this.computerRepository = computerRepository;
		this.companyRepository = companyRepository;
	}

	public List<Computer> getComputers() {
		List<ComputerEntity> computers = computerRepository.selectComputers();
		LOGGER.info("get all Computers from Dao Computer");
		return mapAll(computers);
	}

	public PageDto<Computer> getComputersWithPage(PageDto<Computer> page) {
		if (isValidBean(page)) {
			Pageable pageable = PageRequest.of(page.getNumber(), page.getSize(), Sort.by("name"));
			Page<ComputerEntity> computers = computerRepository.selectComputersWithPage(pageable);
			LOGGER.info("get all Computers with page {} from Dao Computer", page.getNumber());
			return mapAllComputersWithPage(computers);
		}
		return page;
	}
	
	public PageDto<Computer> getComputersWithPageAndSort(PageDto<Computer> page, SortDto sort) {
		if (isValidBean(page)) {
			Pageable pageable = PageRequest.of(
					page.getNumber(),
					page.getSize(),
					Sort.by(getDirection(sort), sort.getProperty()));
			Page<ComputerEntity> computers = computerRepository.selectComputersWithPage(pageable);
			LOGGER.info("get all Computers with page {} from Dao Computer", page.getNumber());
			return mapAllComputersWithPage(computers);
		}
		return page;
	}

	public PageDto<Computer> getSerchComputersWithPage(PageDto<Computer> page, String name) {
		if (isValidBean(page) && !isBlank(name)) {
			Pageable pageable = PageRequest.of(page.getNumber(), page.getSize(), Sort.by("name"));
			Page<ComputerEntity> computers = computerRepository
					.selectSearchComputersWithPage(nameForLikeSql(name), pageable);
			LOGGER.info("get all Computers Serched with page {} from Dao Computer", page.getNumber());
			return mapAllComputersWithPage(computers);
		}
		return page;
	}

	public Optional<Computer> getComputerById(Identifier computerId) {
		Computer computer = null;
		if (isValidBean(computerId)) {
			Optional<ComputerEntity> computerEntity = computerRepository.selectComputerById(computerId.getId());
			LOGGER.info("get Computer with id : {} from Dao Computer", computerId.getId());
			if (computerEntity.isPresent()) {
				computer = mapToComputer(computerEntity.get());
			}
		}
		return Optional.ofNullable(computer);
	}

	public int addComputer(Computer computer) throws NotFoundCompanyException {
		System.err.println(computer);
		int addvalue = 0;
		if (isValidBean(computer)) {
			LOGGER.info("Valid bean Computer .................................................");
			ComputerEntity computerEntity = mapToComputerEntity(computer);
			checkCompany(computer.getIdCompany());
			long idcomputer = computerRepository.getMaxIdComputer() + 1;
			computerEntity.setId(idcomputer);
			addvalue = computerRepository.saveComputer(computerEntity);
		}
		return addvalue;
	}

	private void checkCompany(Long id) throws NotFoundCompanyException {
		if(id != null) {
			Optional<CompanyEntity> company = companyRepository.selectCompanyById(id);
			if (!company.isPresent()) {
					throw new NotFoundCompanyException(
							"Company with id :"+ id +
						    " not Exist referenced by company_id");
			}
		}
	}

	public void deleteComputerById(Identifier computerId) throws NotFoundComputerException {
		if (isValidBean(computerId)) {
			Optional<Computer> getComputer = getComputerById(computerId);
			if (!getComputer.isPresent()) {
				throw new NotFoundComputerException("Compuetr with id :"+ computerId.getId() +" not Exist");
			}
			computerRepository.deleteById(computerId.getId());
		}
	}

	@Transactional
	public void deleteCompany(Identifier idcompany) throws NotFoundCompanyException {
		if (isValidBean(idcompany)) {
			checkCompany(idcompany.getId());
			computerRepository.deleteComputerByCompanyId(idcompany.getId());
			companyRepository.deleteById(idcompany.getId());
		}
	}

	public int updateComputer(Computer computer) throws NotFoundComputerException {
		int updateValue = 0;
		if ( isValidBean(computer)) {
			Optional<Computer> getComputer = checkComputer(computer);
			computer = prepareComputerToUpdate(computer, getComputer.get());
			ComputerEntity entity = mapToComputerEntity(computer);
			updateValue = computerRepository.updateComputer(entity);
		}
		return updateValue;
	}

	private Optional<Computer> checkComputer(Computer computer) throws NotFoundComputerException {
		Identifier computerId = new Identifier(computer.getId());
		Optional<Computer> getComputer = getComputerById(computerId);
		if (!getComputer.isPresent()) {
			throw new NotFoundComputerException("Company with id :"+ computer.getId() +" not Exist");
		}
		return getComputer;
	}

	private Computer prepareComputerToUpdate(Computer computer, Computer getComputer) {
		if (isBlank(computer.getName())) {
			computer.setName(getComputer.getName());
		}
		if (isBlank(computer.getIntroduced())) {
			computer.setIntroduced(getComputer.getIntroduced());
		}
		if (isBlank(computer.getDiscontinued())) {
			computer.setDiscontinued(getComputer.getDiscontinued());	
		}
		if (computer.getIdCompany() == null) {
			computer.setIdCompany(computer.getIdCompany());
		}
		return computer;
	}

	private Direction getDirection(SortDto sort) {
		return sort.getOrder().equals("ASC") ? Direction.ASC : Direction.DESC;
	}

	private boolean isBlank(String element) {
		return element == null || element.isEmpty();
	}
	
	private String nameForLikeSql(String name) {
		return "%" + name.trim() + "%";
	}
}
