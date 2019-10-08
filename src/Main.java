import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import dao.CompanyDao;
import dao.ComputerDao;
import dao.ConnectionToDb;
import dao.Dao;
import dao.DaoFactory;
import dao.TypeDao;
import models.Company;
import models.Computer;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		ConnectionToDb connectionDb = new ConnectionToDb();
		
		DaoFactory factory = new DaoFactory(connectionDb);
	
		ComputerDao computerDao = (ComputerDao) factory.getDao(TypeDao.COMPUTER);
		CompanyDao companyDao =  (CompanyDao) factory.getDao(TypeDao.COMPANY);
		
		
		//List<Computer> computers = computerDao.getComputers();
		Computer computer = computerDao.getComputerById(129);
		System.out.println(computer);
		LocalDate localdate = LocalDate.of(2019, 2, 12);
		computer.setDateIntroduced(localdate);
		computerDao.updateComputer(computer);
		
		
		Company companies = companyDao.getCompanyByID(1);
		
		System.out.println(companies);
		
		
		
	}

}
