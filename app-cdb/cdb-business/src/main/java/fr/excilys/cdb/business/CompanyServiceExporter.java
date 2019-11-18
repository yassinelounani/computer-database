package fr.excilys.cdb.business;

import static fr.excilys.cdb.business.Helper.isValidBean;
import static fr.excilys.cdb.business.Helper.mapAllCompaniesWithPage;
import static fr.excilys.cdb.persistence.mappers.Mapper.mapAll;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.excilys.cdb.api.CompanyService;
import fr.excilys.cdb.api.dto.Company;
import fr.excilys.cdb.api.dto.PageDto;
import fr.excilys.cdb.persistence.models.CompanyEntity;
import fr.excilys.cdb.persistence.repositories.CompanyRepository;

@Service
public class CompanyServiceExporter implements CompanyService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyServiceExporter.class);
	
    
	private CompanyRepository companyRepository;

	@Autowired
	public CompanyServiceExporter(CompanyRepository companyRepository) {
		super();
		this.companyRepository = companyRepository;
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
}
