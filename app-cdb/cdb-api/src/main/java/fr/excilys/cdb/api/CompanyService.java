package fr.excilys.cdb.api;

import java.util.List;
import java.util.Optional;

import fr.excilys.cdb.api.dto.Company;
import fr.excilys.cdb.api.dto.Identifier;
import fr.excilys.cdb.api.dto.Navigation;
import fr.excilys.cdb.api.dto.PageDto;
import fr.excilys.cdb.api.exception.NotFoundCompanyException;

public interface CompanyService {
	/**
	 * get all companies
	 * @return companies list of Companies, null if null
	 */
	List<Company> getCompanies();
	/**
	 * get all company with page
	 * @param page represent number of page and size of page
	 * @return companies list of Companies, null if null
	 */
	PageDto<Company> getCompaniesWithPage(PageDto<Company> page);
	/**
	 * get all Companies with page and sort by properties
	 * @param pageAndSort represent page requested with sort properties
	 * @return Companies list of Companies (may be empty)
	 */
	PageDto<Company> getCompaniesWithPageAndSort(PageDto<Company> page, Navigation navigation);
	/**
	 * get Company with id
	 * @param id of Company to find
	 * @return Optional of Company;
	 */
	Optional<Company> getCompanyById(Identifier CompanyId);
	/**
	 * add Company in database
	 * @param Company to add with all parameters
	 * @return  1 if Company is added else 0
	 */
	int addCompany(Company Company);
	
	/**
	 * update Company
	 * @param Company
	 * @return 1 if Company added else 0 and -1 if error Date
	 * @throws NotFoundCompanyException if id of Company not exist
	 */
	 int updateCompany(Company Company) throws NotFoundCompanyException;
	 
	 /**
	  *  get all Companies Searched with the name of Company or company and page
	  *  @param PageDto contain parameters of pagination of searched page
	  *  @param name name of company to find
	  *  @return Companies list of name Company (may be empty)
	  */
	 PageDto<Company> getSerchCompaniesWithPage(PageDto<Company> page, String name);
}
