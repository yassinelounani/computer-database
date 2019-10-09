package api;

import java.util.List;

import exception.NotFoundCompanyException;
import exception.NotFoundComputerException;
import models.Computer;

public interface ComputerService {
	
	/**
	 * get all computers
	 * @return computers list of computers
	 * @throws NotFoundComputerException throw exception if any computer founded
	 */
	public List<Computer> getComputers() throws NotFoundComputerException;
	/**
	 * get computer with id 
	 * @param id of computer to find
	 * @return computer if is founded else null;
	 * @throws NotFoundComputerException
	 */
	public Computer getComputerById(long id) throws NotFoundComputerException;
	/**
	 * add computer in database
	 * @param computer to add with all parameters
	 * @return -2 if computer is null, 1 if computer added else 0 and -1 if error
	 * @throws NotFoundCompanyException
	 */
	public int addComputer(Computer computer) throws NotFoundCompanyException;
	/**
	 * delete computer with id
	 * @param id
	 * @return 2 if id is <=0, 1 if computer deleted else 0 and -1 if error
	 */
	public int deleteComputerById(long id);
	
	/**
	 * update computer 
	 * @param computer
	 * @return -2 if computer is null, 1 if computer added else 0 and -1 if error
	 */
	public int updateComputer(Computer computer) throws NotFoundComputerException;
	
}
