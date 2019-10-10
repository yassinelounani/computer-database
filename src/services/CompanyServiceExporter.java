package services;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import api.CompanyService;
import api.Page;
import dao.CompanyDao;
import exception.BadNumberPageException;
import exception.NotFoundCompanyException;
import models.Company;

public class CompanyServiceExporter implements CompanyService{
	
	private static final Logger LOGGER = Logger.getLogger(CompanyServiceExporter.class);
	
	private CompanyDao companyDao;

	public CompanyServiceExporter(CompanyDao companyDao) {
		super();
		this.companyDao = companyDao;
	}
	
	public List<Company> getCompanies() throws NotFoundCompanyException {
		
		try{
			List<Company> companies = companyDao.getCompanies();
			if(companies.isEmpty()) {
				throw new NotFoundCompanyException("Any Company founded in DataBase");
			}
			LOGGER.info("get all Company from Dao Company");
			return companies; 
			
		} catch (SQLException e) {
			e.getMessage();
		}
		return null;
	}
	public List<Company> getCompaniesWithPage(Page page) throws NotFoundCompanyException, BadNumberPageException {
		try{
			List<Company> companies = companyDao.getCompaniesWithPage(page);
			if(companies.isEmpty()) {
				throw new NotFoundCompanyException("Any Company founded in DataBase");
			}
			LOGGER.info("get all Company page " + page.getNumber() + " from Dao Company");
			return companies; 
			
		} catch (SQLException e) {
			e.getMessage();
		}
		return null;
		
	}
}
