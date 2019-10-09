import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import api.CompanyService;
import api.ComputerService;
import dao.CompanyDao;
import dao.ComputerDao;
import dao.ConnectionToDb;
import dao.DaoFactory;
import dao.TypeDao;
import exception.DateBeforeDiscontinuedException;
import exception.NotFoundCompanyException;
import exception.NotFoundComputerException;
import models.Company;
import models.Computer;
import services.CompanyServiceExporter;
import services.ComputerServiceExporter;
import wrappers.HelperDate;

public class Main {
	
	private static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		int choice = 0;
		
		ConnectionToDb connectionDb = new ConnectionToDb();
		
		DaoFactory factory = new DaoFactory(connectionDb);
	
		ComputerDao computerDao = (ComputerDao) factory.getDao(TypeDao.COMPUTER);
		CompanyDao companyDao =  (CompanyDao) factory.getDao(TypeDao.COMPANY);
		
		ComputerService computerService = new ComputerServiceExporter(computerDao, companyDao);
		CompanyService companyService = new CompanyServiceExporter(companyDao);
		
	LABEL :	while(true) {
			printMenu();
			choice = insertChoice();
			
			switch(choice) {
				case 1 : printListComputers(computerService); break;
				case 2 : printListCompanies(companyService); break;
				case 3 : printDetailsComputer(computerService); break;
				case 4 : createComputer(computerService); break;
				case 5 : updateComputer(computerService); break;
				case 6 : deleteComputer(computerService); break;
				case 0 : break LABEL;
			}
		}
		System.out.println("\n Tank You For Chosing EXCILYS");
		
		
		
	}
	public static void printMenu() {
		System.out.println("====================================================");
		System.out.println("                    MENU                             ");
		System.out.println("====================================================");
		System.out.println("Choose one option please, Entrer number of operation.");
		System.out.println("1 - show list of computers");
		System.out.println("2 - show list companies");
		System.out.println("3 - show details of selected Computer");
		System.out.println("4 - create computer");
		System.out.println("5 - update computer");
		System.out.println("6 - delete computer");
		System.out.println("0 - quit");
		System.out.println("====================================================\n");
		System.out.print(" your choice pleace ? : ");
		
	}
	
	public static void printListComputers(ComputerService computerService) {
		try {
			List<Computer> computers = computerService.getComputers();
			computers.forEach(System.out::println);
		} catch (NotFoundComputerException e) {
			e.getMessage();
		}
	}
	
	public static void printListCompanies(CompanyService companyService) {
		try {
			List<Company> companies = companyService.getCompanies();
			companies.forEach(System.out::println);
		} catch (NotFoundCompanyException e) {
			e.getMessage();
		}
	}
	
	public static void printDetailsComputer(ComputerService computerService) {
		System.out.print("Entrer l'ID du computer : ");
		long id = sc.nextLong();
		try {
			Computer computer = computerService.getComputerById(id);
			System.out.println(computer);
		} catch (NotFoundComputerException e) {
			e.getMessage();
		}
	}
	public static int insertChoice() {
		boolean hasInt = false;
		int choice = 0;
		do {
			
			hasInt = sc.hasNextInt();
			if(!hasInt) {
				continue;
			}
			choice = sc.nextInt();
			sc.nextLine();
		}while(choice < 1 || choice > 6);
		return choice;
	}
	public static void createComputer(ComputerService computerService) {
		System.out.print("Please Entrer id of of Computer : ");
		long id = sc.nextLong();
		sc.nextLine();
		System.out.print("Please Entrer name Computer :");
		String name = sc.nextLine();
		System.out.print("Please Entrer date introduced of Computer (format yyyy-mm-dd or dd/mm/yyyy) :");
		String stringDateIntroduced = sc.nextLine();
		System.out.print("Please Entrer date discontinued of Computer (format yyyy-mm-dd or dd/mm/yyyy) :");
		String StringDateDiscontinued = sc.nextLine();
		System.out.print("Please Entrer id of company of Computer if not please entre 0:");
		long company_id = sc.nextLong();
		LocalDate dateIntroduced = HelperDate.StringDateToLocalDate(stringDateIntroduced);
		LocalDate dateDiscontinued = HelperDate.StringDateToLocalDate(StringDateDiscontinued);
		Company company = new Company(company_id, null);
		Computer computer = new Computer(id, name, dateIntroduced, dateDiscontinued, company);
		try {
			int addCmputer = computerService.addComputer(computer);
			switch(addCmputer) {
			case 0 : System.out.println("Computer is not added"); break;
			case 1 : System.out.println("Computer is added"); break;
			case 2 : System.out.println("your id is negative or null"); break;
			}
		} catch (NotFoundCompanyException e) {
			e.getMessage();
		}	
	}
	
	public static void deleteComputer(ComputerService computerService) {
		System.out.print("Please Entrer id of of Computer to delete :");
		long id = sc.nextLong();
		int delete = computerService.deleteComputerById(id);
		switch(delete) {
			case 0 : System.out.println("Computer is not deleted"); break;
			case 1 : System.out.println("Computer is deleted"); break;
			case 2 : System.out.println("your id is negative or null"); break;
		}	
	}
	
	public static void updateComputer(ComputerService computerService) {
		System.out.print("Please Entrer id of of Computer to update :");
		long id = sc.nextLong();
		sc.nextLine();
		System.out.print("Please Entrer new name for Computer:");
		String name = sc.nextLine();
		System.out.print("Please Entrer new date introduced of Computer (format yyyy-mm-dd or dd/mm/yyyy) :");
		String stringDateIntroduced = sc.nextLine();
		System.out.print("Please Entrer new date discontinued of Computer (format yyyy-mm-dd or dd/mm/yyyy) :");
		String StringDateDiscontinued = sc.nextLine();
		System.out.print("Please Entrer id of company of Computer if not please entre 0:");
		long company_id = sc.nextLong();
		
		LocalDate dateIntroduced = HelperDate.StringDateToLocalDate(stringDateIntroduced);
		LocalDate dateDiscontinued = HelperDate.StringDateToLocalDate(StringDateDiscontinued);
		
		Company company = new Company(company_id, null);
		Computer computer = new Computer(name);
		computer.setId(id);
		computer.setDateIntroduced(dateIntroduced);
		try{
			computer.setDateDiscontinued(dateDiscontinued);
		}catch (DateBeforeDiscontinuedException e) {
			e.getMessage();
		}
		computer.setCompany(company);
		
		try {
			int update = computerService.updateComputer(computer);
			switch(update) {
				case 0 : System.out.println("computer not updated"); break;
				case 1 : System.out.println("Computer is updated"); break;
				case 2 : System.out.println("computer to update is null"); break;
			}
		} catch (NotFoundComputerException e) {
			e.getMessage();
		} 
		
	}
	
	
}
