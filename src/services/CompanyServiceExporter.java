package services;

import java.sql.SQLException;
import java.util.List;

import api.CompanyService;
import dao.CompanyDao;
import exception.NotFoundCompanyException;
import models.Company;

public class CompanyServiceExporter implements CompanyService{
	
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
			return companies; 
			
		} catch (SQLException e) {
			e.getMessage();
		}
		return null;
	}
}
