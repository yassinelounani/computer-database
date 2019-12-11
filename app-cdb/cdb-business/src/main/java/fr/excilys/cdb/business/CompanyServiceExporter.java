package fr.excilys.cdb.business;

import static fr.excilys.cdb.business.Helper.isValidBean;
import static fr.excilys.cdb.business.Helper.mapAllCompaniesWithPage;
import static fr.excilys.cdb.business.Helper.mapToCompany;
import static fr.excilys.cdb.business.Helper.mapToCompanyEntity;
import static fr.excilys.cdb.persistence.mappers.Mapper.mapAll;

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

import fr.excilys.cdb.api.CompanyService;
import fr.excilys.cdb.api.dto.Company;
import fr.excilys.cdb.api.dto.Identifier;
import fr.excilys.cdb.api.dto.Navigation;
import fr.excilys.cdb.api.dto.PageDto;
import fr.excilys.cdb.api.exception.NotFoundCompanyException;
import fr.excilys.cdb.persistence.models.CompanyEntity;
import fr.excilys.cdb.persistence.repositories.CompanyRepository;
import fr.excilys.cdb.persistence.repositories.ComputerRepository;

@Service
public class CompanyServiceExporter implements CompanyService{
	
	private static final String NAME = "name";


	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyServiceExporter.class);
	
    
	private CompanyRepository companyRepository;
	private ComputerRepository computerRepository;

	@Autowired
	public CompanyServiceExporter(CompanyRepository companyRepository, ComputerRepository computerRepository) {
		super();
		this.companyRepository = companyRepository;
		this.computerRepository = computerRepository;
	}

	public List<Company> getCompanies() {
		List<CompanyEntity> companies = companyRepository.selectCompanies();
		LOGGER.info("get all Company from Dao Company");
		return mapAll(companies, Company.class);
	}

	public PageDto<Company> getCompaniesWithPage(PageDto<Company> page) {
		if (isValidBean(page)) {
			Pageable pageable = PageRequest.of(page.getNumber(), page.getSize());
			Page<CompanyEntity> companies = companyRepository.selectCompaniesWithPage(pageable);
			LOGGER.info("get all Company page {} from Dao Company", page.getNumber());
			return mapAllCompaniesWithPage(companies);
		}
		return page;
	}

	public PageDto<Company> getCompaniesWithPageAndSort(PageDto<Company> page, Navigation navigation) {
		if (isValidBean(page)) {
			Page<CompanyEntity> companies = null;
			Sort sort = Sort.by(getDirection(navigation.getOrder()), NAME);
			Pageable pageable = PageRequest.of(
					page.getNumber(),
					page.getSize(),
					sort);
			if (navigation.getValue() == null || navigation.getValue().isEmpty()) {
				companies = companyRepository.selectCompaniesWithPage(pageable);
			} else {
				return getSerchCompaniesWithPage(page, navigation.getValue(), sort);
			}
			LOGGER.info("get all companies with page {} from Dao Computer", page.getNumber());
			return mapAllCompaniesWithPage(companies);
		}
		return page;
	}

	private PageDto<Company> getSerchCompaniesWithPage(PageDto<Company> page, String name, Sort sort) {
		if (isValidBean(page) && !isBlank(name)) {
			Pageable pageable = PageRequest.of(page.getNumber(), page.getSize(), sort);
			Page<CompanyEntity> companies = companyRepository.selectSearchCompaniesByPage(nameForLikeSql(name), pageable);
			LOGGER.info("get all companies Serched with page {} from Dao Computer", page.getNumber());
			return mapAllCompaniesWithPage(companies);
		}
		return page;
	}

	public Optional<Company> getCompanyById(Identifier companyId) {
		Company company = null;
		if (isValidBean(companyId)) {
			Optional<CompanyEntity> companyEntity = companyRepository.selectCompanyById(companyId.getId());
			LOGGER.info("get company with id : {} from Dao Computer", companyId.getId());
			if (companyEntity.isPresent()) {
				company = mapToCompany(companyEntity.get());
			}
		}
		return Optional.ofNullable(company);
	}

	public int addCompany(Company company) {
		int addvalue = 0;
		if (isValidBean(company)) {
			LOGGER.info("Valid bean Computer .................................................");
			CompanyEntity companyEntity = mapToCompanyEntity(company);
			long idcompany = companyRepository.getMaxIdCompanies() + 1;
			companyEntity.setId(idcompany);
			addvalue = companyRepository.saveCompany(companyEntity);
		}
		return addvalue;
	}
	
	@Transactional
	public void deleteCompany(Identifier idcompany) throws NotFoundCompanyException {
		if (isValidBean(idcompany)) {
			Optional<Company> getCompany = getCompanyById(idcompany);
			if (!getCompany.isPresent()) {
				throw new NotFoundCompanyException("Company with id :"+ idcompany.getId() +" not Exist");
			} else {
				computerRepository.deleteComputerByCompanyId(idcompany.getId());
				companyRepository.deleteById(idcompany.getId());
			}
		}
	}
	
	public int updateCompany(Company company) throws NotFoundCompanyException {
		int updateValue = 0;
		if ( isValidBean(company)) {
			Identifier companyId = new Identifier(company.getId());
			Optional<Company> getCompany = getCompanyById(companyId);
			if (!getCompany.isPresent()) {
				throw new NotFoundCompanyException("Company with id :"+ company.getId() +" not Exist");
			}
			
			CompanyEntity entity = mapToCompanyEntity(company);
			updateValue = companyRepository.updateCompany(entity);
		}
		return updateValue;
	}

	private Direction getDirection(String proprerty) {
		return proprerty.equals("ASC") ? Direction.ASC : Direction.DESC;
	}

	private boolean isBlank(String name) {
		return name == null || name.isEmpty();
	}

	private String nameForLikeSql(String name) {
		return "%" + name.trim() + "%";
	}
}
