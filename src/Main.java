import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.BasicConfigurator;

import api.CompanyService;
import api.ComputerService;
import api.Page;
import dao.CompanyDao;
import dao.ComputerDao;
import dao.ConnectionToDb;
import dao.DaoFactory;
import dao.TypeDao;
import exception.BadNumberPageException;
import exception.BadSizePageException;
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
		BasicConfigurator.configure();
		int choice = 0;
		
		ConnectionToDb connectionDb = new ConnectionToDb();
		
		DaoFactory factory = new DaoFactory(connectionDb);
	
		ComputerDao computerDao = (ComputerDao) factory.getDao(TypeDao.COMPUTER);
		CompanyDao companyDao =  (CompanyDao) factory.getDao(TypeDao.COMPANY);
		
		ComputerService computerService = new ComputerServiceExporter(computerDao, companyDao);
		CompanyService companyService = new CompanyServiceExporter(companyDao);
		
		boolean isContinue = true;
	
		LABEL :	while(isContinue) {
			printMenu();
			choice = insertChoice();
			
			switch(choice) {
				case 1 : printListComputers(computerService); break;
				case 2 : printListCompanies(companyService); break;
				case 3 : printDetailsComputer(computerService); break;
				case 4 : createComputer(computerService); break;
				case 5 : updateComputer(computerService); break;
				case 6 : deleteComputer(computerService); break;
				case 0 : System.out.println("\n Tank You For chosing EXCILYS\n"); break LABEL;
			}
			
		}
		System.out.println("End Propram.....");
		
		
		
		
		
	}
	public static void printMenu() {
		System.out.println("====================================================");
		System.out.println("                    MENU                             ");
		System.out.println("====================================================");
		System.out.println("Choose one option please, Entrer number of operation.");
		System.out.println("1 - Show list of computers");
		System.out.println("2 - Show list companies");
		System.out.println("3 - Show details of selected Computer");
		System.out.println("4 - Create computer");
		System.out.println("5 - Update computer");
		System.out.println("6 - Delete computer");
		System.out.println("0 - Quit");
		System.out.println("====================================================\n");
		System.out.print(" Your choice pleace ? : ");
		
	}
	
	public static void printComputers(ComputerService computerService) {
		try {
			List<Computer> computers = computerService.getComputers();
			computers.forEach(System.out::println);
		} catch (NotFoundComputerException e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	public static void printComputerswithPage(ComputerService computerService) {
		Page page = askPage();
		if(page != null) {
			try {
				List<Computer> computers = computerService.getComputersWithPage(page);
				computers.forEach(System.out::println);
			} catch (NotFoundComputerException e) {
				System.out.println(e.getMessage());
			} catch (BadNumberPageException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public static Page askPage() {
		System.out.print("Please Entrer your number page (!! first page begin at 1 !!): ");
		int numberPage = sc.nextInt();
		System.out.print("Please Entrer your size page : ");
		int sizePage = sc.nextInt();
		
		try {
			return new Page(numberPage, sizePage);
		} catch (BadNumberPageException e) {
			System.out.println(e.getMessage());
			return null;
		}catch (BadSizePageException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public static boolean isListWithPage() {
		System.out.println("Would you prefer get List with Page (0 => yes or 1 => no)");
		int choice = sc.nextInt();
		return choice == 0 ? true : false; 
	}
	
	public static void printListComputers(ComputerService computerService) {
		if(isListWithPage()) {
			printComputerswithPage(computerService);
		} else {
			printComputers(computerService);
		}
	}
	
	public static void printListCompanies(CompanyService companyService) {
		if(isListWithPage()) {
			printCompaniesWithPage(companyService);
		} else {
			printCompanies(companyService);
		}
	}
	
	public static void printCompanies(CompanyService companyService) {
		try {
			List<Company> companies = companyService.getCompanies();
			companies.forEach(System.out::println);
		} catch (NotFoundCompanyException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void printCompaniesWithPage(CompanyService companyService) {
		Page page = askPage();
		if(page != null) {
			try {
				List<Company> companies = companyService.getCompaniesWithPage(page);
				companies.forEach(System.out::println);
			} catch (NotFoundCompanyException e) {
				System.out.println(e.getMessage());
			}catch(BadNumberPageException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public static void printDetailsComputer(ComputerService computerService) {
		System.out.print("Entrer l'ID du computer : ");
		long id = sc.nextLong();
		try {
			Computer computer = computerService.getComputerById(id);
			System.out.println(computer);
		} catch (NotFoundComputerException e) {
			System.out.println(e.getMessage());
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
		}while(choice < 0 || choice > 6);
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
		if(! dateIntroduced.isBefore(dateDiscontinued)) {
			System.out.println("Date Disontinued (" + dateDiscontinued + ") is not after (" + dateIntroduced + ")");
			System.out.println("Computer is not added");
			return;
		}
		Company company = new Company(company_id, null);
		Computer computer = new Computer(id, name, dateIntroduced, dateDiscontinued, company);
		try {
			int addCmputer = computerService.addComputer(computer);
			switch(addCmputer) {
			case 0 : System.out.println("Computer is not added"); break;
			case 1 : System.out.println("Computer is added"); break;
			case 2 : System.out.println("Your id is negative or null"); break;
			}
		} catch (NotFoundCompanyException e) {
			System.out.println(e.getMessage());
		}	
	}
	
	public static void deleteComputer(ComputerService computerService) {
		System.out.print("Please Entrer id of of Computer to delete :");
		long id = sc.nextLong();
		int delete = computerService.deleteComputerById(id);
		switch(delete) {
			case 0 : System.out.println("Computer is not deleted"); break;
			case 1 : System.out.println("Computer is deleted"); break;
			case 2 : System.out.println("Your id is negative or null"); break;
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
			System.out.println(e.getMessage());
			System.out.println("Computer not updated");
			return;
		}
		computer.setCompany(company);
		
		try {
			int update = computerService.updateComputer(computer);
			switch(update) {
				case -1 : System.out.println("Error update in date discontinued "); break;
				case 0 : System.out.println("Computer not updated"); break;
				case 1 : System.out.println("Computer is updated"); break;
				case 2 : System.out.println("Computer to update is null"); break;
			}
		} catch (NotFoundComputerException e) {
			System.out.println(e.getMessage());
		} 
		
	}
	
	
}
