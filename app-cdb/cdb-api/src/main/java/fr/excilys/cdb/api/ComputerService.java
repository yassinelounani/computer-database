package fr.excilys.cdb.api;

import java.util.List;
import java.util.Optional;

import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.api.dto.Identifier;
import fr.excilys.cdb.api.dto.NameAndPage;
import fr.excilys.cdb.api.dto.Page;
import fr.excilys.cdb.api.dto.PageAndSort;
import fr.excilys.cdb.api.exception.NotFoundCompanyException;
import fr.excilys.cdb.api.exception.NotFoundComputerException;

public interface ComputerService {
	/**
	 * get all computers
	 * @return computers list of computers list (may be empty)
	 */
	List<Computer> getComputers();
	/**
	 * get all computers with page
	 * @param page represent number of page and size of page
	 * @return computers list of computers (may be empty)
	 */
	List<Computer> getComputersWithPage(Page page);
	/**
	 * get all computers with page and sort by properties
	 * @param pageAndSort represent page requested with sort properties
	 * @return computers list of computers (may be empty)
	 */
	List<Computer> getComputersWithPageAndSort(PageAndSort pageAndSort);
	/**
	 * get number of page with size in parameter
	 * @param size of page
	 * @return total page 
	 */
	int getTotalPagesOfComputers(Page size);
	/**
	 * get number of page with the name searched
	 * @param size of page
	 * @return total page 
	 */
	public int getTotalPagesOfSerchedComputers(NameAndPage nameAndPage);
	/**
	 * get computer with id
	 * @param id of computer to find
	 * @return Optional of computer;
	 */
	Optional<Computer> getComputerById(Identifier computerId);
	/**
	 * add computer in database
	 * @param computer to add with all parameters
	 * @return  1 if computer is added else 0
	 */
	int addComputer(Computer computer) throws NotFoundCompanyException;
	/**
	 * delete computer with id
	 * @param computerId id of computer
	 * @throws NotFoundComputerException if id of computer not exist
	 * @return 1 if computer deleted else 0
	 */
	int deleteComputerById(Identifier computerId) throws NotFoundComputerException;
	/**
	 * delete company and computers associates with id of company
	 * @param companyId id of company
	 * @throws NotFoundComputerException if id of computer not exist
	 * @return 1 if computer deleted else 0
	 */
	int deleteCompany(Identifier companyId) throws NotFoundCompanyException;
	/**
	 * update computer
	 * @param computer
	 * @return 1 if computer added else 0 and -1 if error Date
	 * @throws NotFoundComputerException if id of computer not exist
	 */
	 int updateComputer(Computer computer) throws NotFoundComputerException;
	 
	 /**
	  *  get all computers Searched with the name of computer or company and page
	  *  @param nameAndPAge contain name of computer to search and page
	  *  @return computers list of name computer (may be empty)
	  */
	 List<Computer> getSerchComputersWithPage(NameAndPage nameAndPAge);
}
