package api;

import java.util.List;

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

}
