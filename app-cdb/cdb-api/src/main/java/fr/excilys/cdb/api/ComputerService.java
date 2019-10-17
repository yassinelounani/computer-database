package fr.excilys.cdb.api;

import java.util.List;
import java.util.Optional;

import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.api.dto.Page;
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
	 * get computer with id
	 * @param id of computer to find
	 * @return Optional of computer;
	 */
	Optional<Computer> getComputerById(long id);
	/**
	 * add computer in database
	 * @param computer to add with all parameters
	 * @return  1 if computer is added else 0
	 */
	int addComputer(Computer computer) throws NotFoundCompanyException;
	/**
	 * delete computer with id
	 * @param id
	 * @throws NotFoundComputerException if id of computer not exist
	 * @return 1 if computer deleted else 0
	 */
	int deleteComputerById(long id) throws NotFoundComputerException;
	/**
	 * update computer
	 * @param computer
	 * @return 1 if computer added else 0 and -1 if error Date
	 * @throws NotFoundComputerException if id of computer not exist
	 */
	 int updateComputer(Computer computer) throws NotFoundComputerException;
}
