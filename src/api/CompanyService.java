package api;

import java.util.List;

import exception.BadNumberPageException;
import exception.NotFoundCompanyException;
import exception.NotFoundComputerException;
import models.Company;

public interface CompanyService {
	
	/**
	 * get all company
	 * @return company list of computers, null if null
	 * @throws NotFoundComputerException throw exception if any computer founded
	 */
	public List<Company> getCompanies() throws NotFoundCompanyException;
	
	/**
	 * get all company with page
	 * @param page represent number of page and size of page
	 * @return company list of computers, null if null
	 * @throws NotFoundCompanyException throw exception if any computer founded
	 */
	public List<Company> getCompaniesWithPage(Page page) throws NotFoundCompanyException, BadNumberPageException;

}
