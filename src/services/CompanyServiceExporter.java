package services;

import java.util.List;
import org.apache.log4j.Logger;
import api.CompanyService;
import api.Page;
import dao.CompanyDao;
import models.Company;

public class CompanyServiceExporter implements CompanyService{
	
	private static final Logger LOGGER = Logger.getLogger(CompanyServiceExporter.class);
	
	private CompanyDao companyDao;

	public CompanyServiceExporter(CompanyDao companyDao) {
		super();
		this.companyDao = companyDao;
	}
	
	public List<Company> getCompanies() {
			List<Company> companies = companyDao.getCompanies();
			LOGGER.info("get all Company from Dao Company");
			return companies;
	}
	public List<Company> getCompaniesWithPage(Page page) {
			List<Company> companies = companyDao.getCompaniesWithPage(page);
			LOGGER.info("get all Company page " + page.getNumber() + " from Dao Company");
			return companies;
	}
}
