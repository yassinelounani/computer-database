package fr.excilys.cdb.business;

import static fr.excilys.cdb.business.Helper.isValidBean;
import static fr.excilys.cdb.persistence.mappers.Mapper.mapAll;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.excilys.cdb.api.CompanyService;
import fr.excilys.cdb.api.dto.Company;
import fr.excilys.cdb.api.dto.Page;
import fr.excilys.cdb.persistence.dao.CompanyDao;
import fr.excilys.cdb.persistence.models.CompanyEntity;
import fr.excilys.cdb.persistence.models.Pageable;

@Service
public class CompanyServiceExporter implements CompanyService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyServiceExporter.class);
	
    @Autowired
	private CompanyDao companyDao;

	public List<Company> getCompanies() {
		List<CompanyEntity> companies = companyDao.getCompanies();
		LOGGER.info("get all Company from Dao Company");
		return mapAll(companies, Company.class);
	}

	public List<Company> getCompaniesWithPage(Page page) {
		if (isValidBean(page)) {
			Pageable pageable = new Pageable(page.getNumber(), page.getSize());;
			List<CompanyEntity> companies = companyDao.getCompaniesWithPage(pageable);
			LOGGER.info("get all Company page {} from Dao Company", page.getNumber());
			return mapAll(companies, Company.class);
		}
		return new ArrayList<>();
	}
}
