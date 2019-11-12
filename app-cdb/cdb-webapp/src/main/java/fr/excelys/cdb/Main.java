package fr.excelys.cdb;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import fr.excilys.cdb.api.CompanyService;
import fr.excilys.cdb.api.ComputerService;
import fr.excilys.cdb.api.dto.Company;
import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.api.dto.Computer.Builder;
import fr.excilys.cdb.api.dto.Identifier;
import fr.excilys.cdb.api.dto.Page;
import fr.excilys.cdb.api.exception.NotFoundCompanyException;
import fr.excilys.cdb.api.exception.NotFoundComputerException;

public class Main {
	private static Scanner sc = new Scanner(System.in);
	private static final String NO_COMPUTERS = "No computer Founded";
	private static final String NO_COMPANIES = "No computer Founded";
	private static final String ASK_ID_COMPUTER = "Please Entrer id of of Computer : ";
	private static final String ASK_ID_COMPANY = "Please Entrer id of of Company : ";
	private static final String ASK_NAME_COMPUTER = "Please Entrer Name of Computer : ";
	private static final String ASK_DATE_INTRODUCED = "Please Entrer date introduced of Computer (format yyyy-mm-dd or dd/mm/yyyy) :";
	private static final String ASK_DATE_DISCONTINUED = "Please Entrer date discontinued of Computer (format yyyy-mm-dd or dd/mm/yyyy) :";
	private static final String ASK_COMPANY_ID = "Please Entrer id of company of Computer if not please entre 0:";
	private static final String ID_NOT_BE_NULL = "Id Not be null or Negative";

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		int choice = 0;

		ComputerService computerService = null;
		CompanyService companyService = null;

		boolean isContinue = true;
		LABEL :	while (isContinue) {
			printMenu();
			choice = insertChoice();
			switch (choice) {
				case 1 : printListComputers(computerService); break;
				case 2 : printListCompanies(companyService); break;
				case 3 : printDetailsComputer(computerService); break;
				case 4 : createComputer(computerService); break;
				case 5 : updateComputer(computerService); break;
				case 6 : deleteComputer(computerService); break;
				case 7 : deleteCompany(computerService); break;
				case 0 : System.out.println("\n Tank You For chosing EXCILYS\n"); break LABEL;
				default : System.out.println("Your Choice not exist please tray again \n"); break LABEL;
			}
		}
		System.out.println("End Propram.....");
	}

	public static void printMenu() {
		System.out.println("\n====================================================");
		System.out.println("                    MENU                             ");
		System.out.println("====================================================");
		System.out.println("Choose one option please, Entrer number of operation.");
		System.out.println("1 - Show list of computers");
		System.out.println("2 - Show list companies");
		System.out.println("3 - Show details of selected Computer");
		System.out.println("4 - Create computer");
		System.out.println("5 - Update computer");
		System.out.println("6 - Delete computer");
		System.out.println("7 - Delete company");
		System.out.println("0 - Quit");
		System.out.println("====================================================\n");
		System.out.print(" Your choice pleace ? : ");
	}

	public static void printComputers(ComputerService computerService) {
		List<Computer> computers = computerService.getComputers();
		printList(computers, NO_COMPUTERS);
	}

	public static <E> void printList(List<E> list, String message) {
		if (list.isEmpty()) {
			System.out.println(message);
			return;
		}
		list.forEach(System.out::println);
	}

	public static void printComputerswithPage(ComputerService computerService) {
		Optional<Page> page = askPage();
		if (page.isPresent()) {
			List<Computer> computers = computerService.getComputersWithPage(page.get());
			printList(computers, NO_COMPUTERS);
		}
	}

	public static Optional<Page> askPage() {
		System.out.print("Please Entrer your number page (!! first page begin at 1 !!): ");
		int numberPage = sc.nextInt();
		if (numberPage < 1) {
			System.out.println("Sorry the number of Page start with number 1");
			return Optional.empty();
		}
		System.out.print("Please Entrer your size page : ");
		int sizePage = sc.nextInt();
		return Optional.ofNullable(new Page(numberPage, sizePage));
	}

	public static boolean isListWithPage() {
		System.out.println("Would you prefer get List with Page (0 => yes or 1 => no)");
		int choice = sc.nextInt();
		return choice == 0 ? true : false;
	}

	public static void printListComputers(ComputerService computerService) {
		if (isListWithPage()) {
			printComputerswithPage(computerService);
		} else {
			printComputers(computerService);
		}
	}

	public static void printListCompanies(CompanyService companyService) {
		if (isListWithPage()) {
			printCompaniesWithPage(companyService);
		} else {
			printCompanies(companyService);
		}
	}

	public static void printCompanies(CompanyService companyService) {
		List<Company> companies = companyService.getCompanies();
		printList(companies, NO_COMPANIES);
	}
	public static void printCompaniesWithPage(CompanyService companyService) {
		Optional<Page> page = askPage();
		if (page.isPresent()) {
			List<Company> computers = companyService.getCompaniesWithPage(page.get());
			printList(computers, NO_COMPUTERS);
		}
	}

	public static void printDetailsComputer(ComputerService computerService) {
		System.out.print("Entrer l'ID du computer : ");
		long id = sc.nextLong();
		if (id < 1) {
			System.out.print(ID_NOT_BE_NULL);
			return;
		}
		Identifier computerId = new Identifier(id);
		Optional<Computer> computer = computerService.getComputerById(computerId);
		if (computer.isPresent()) {
			System.out.println("\n" + computer.get());
		} else {
			System.out.println(NO_COMPUTERS);
		}
	}

	public static int insertChoice() {
		boolean hasInt = false;
		int choice = 0;
		do {
			hasInt = sc.hasNextInt();
			if (!hasInt) {
				continue;
			}
			choice = sc.nextInt();
			sc.nextLine();
		} while (choice < 0 || choice > 7);
		return choice;
	}

	public static void createComputer(ComputerService computerService) {
		System.out.println("......................ADD COMPUTER..........................");
		Optional<Computer> computer = askComputer();
		if (!computer.isPresent()) {
			return;
		}
		try {
				int addCmputer = computerService.addComputer(computer.get());
				switch (addCmputer) {
					case 0 : System.out.println("Computer is not added"); break;
					case 1 : System.out.println("Computer is added"); break;
				}
		} catch (NotFoundCompanyException e) {
				System.out.println(e.getMessage());
		}
	}

	public static Optional<Computer> askComputer() {
		System.out.print(ASK_ID_COMPUTER);
		long id = sc.nextLong();
		sc.nextLine();
		if (id <= 0) {
			System.out.println(ID_NOT_BE_NULL);
			return Optional.empty();
		}
		System.out.print(ASK_NAME_COMPUTER);
		String name = sc.nextLine();
		System.out.print(ASK_DATE_INTRODUCED);
		String dateIntroduced = sc.nextLine();
		System.out.print(ASK_DATE_DISCONTINUED);
		String dateDiscontinued = sc.nextLine();
		System.out.print(ASK_COMPANY_ID);
		long companyId = sc.nextLong();
		
		Computer computer = Builder.newInstance()
										  .setId(id)
										  .setName(name)
										  .setIntroduced(dateIntroduced)
										  .setDicontinued(dateDiscontinued)
										  .setIdCompany(companyId)
										  .build();
		return Optional.ofNullable(computer);
	}

	public static void deleteComputer(ComputerService computerService) {
		System.out.println("......................DELETE COMPUTER..........................");
		System.out.print(ASK_ID_COMPUTER);
		long id = sc.nextLong();
		if (id <= 0) {
			System.out.println(ID_NOT_BE_NULL);
			return;
		}
		Identifier computerId = new Identifier(id);
		int delete = 0;
		try {
			delete = computerService.deleteComputerById(computerId);
		} catch (NotFoundComputerException e) {
			System.out.println(e.getMessage());
			return;
		}
		switch (delete) {
			case 0 : System.out.println("Computer is not deleted"); break;
			case 1 : System.out.println("Computer is deleted"); break;
		}
	}

	public static void deleteCompany(ComputerService computerService) {
		System.out.println("......................DELETE COMPANY ..........................");
		System.out.print(ASK_ID_COMPANY);
		long id = sc.nextLong();
		if (id <= 0) {
			System.out.println(ID_NOT_BE_NULL);
			return;
		}
		Identifier companyId = new Identifier(id);
		int delete = 0;
		try {
			delete = computerService.deleteCompany(companyId);
		} catch (NotFoundCompanyException e) {
			System.out.println(e.getMessage());
			return;
		}
		switch (delete) {
			case 0 : System.out.println("Computer is not deleted"); break;
			case 1 : System.out.println("Computer is deleted"); break;
		}
	}

	public static void updateComputer(ComputerService computerService) {
		System.out.println("......................UPDATE COMPUTER..........................");
		Optional<Computer> computer = askComputer();
		if (!computer.isPresent()) {
			return;
		}
		try {
			int update = computerService.updateComputer(computer.get());
			switch (update) {
				case 0 : System.out.println("Computer not updated"); break;
				case 1 : System.out.println("Computer is updated"); break;
			}
		} catch (NotFoundComputerException e) {
			System.out.println(e.getMessage());
		}
	}

}
