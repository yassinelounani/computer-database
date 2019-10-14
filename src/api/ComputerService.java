package api;

import java.util.List;
import java.util.Optional;

import exception.NotFoundCompanyException;
import exception.NotFoundComputerException;
import models.Computer;

public interface ComputerService {
	
	/**
	 * get all computers
	 * @return computers list of computers list (may be empty)
	 */
	public List<Computer> getComputers();
	
	/**
	 * get all computers with page
	 * @param page represent number of page and size of page
	 * @return computers list of computers (may be empty)
	 */
	public List<Computer> getComputersWithPage(Page page);
	/**
	 * get computer with id 
	 * @param id of computer to find
	 * @return Optional of computer;
	 */
	public Optional<Computer> getComputerById(long id);
	/**
	 * add computer in database
	 * @param computer to add with all parameters
	 * @return  1 if computer is added else 0
	 */
	public int addComputer(Computer computer) throws NotFoundCompanyException;
	/**
	 * delete computer with id
	 * @param id
	 * @throws NotFoundComputerException if id of computer not exist
	 * @return 1 if computer deleted else 0
	 */
	public int deleteComputerById(long id) throws NotFoundComputerException;
	
	/**
	 * update computer 
	 * @param computer
	 * @return 1 if computer added else 0 and -1 if error Date
	 * @throws NotFoundComputerException if id of computer not exist
	 */
	public int updateComputer(Computer computer) throws NotFoundComputerException;
	
	
	
}
