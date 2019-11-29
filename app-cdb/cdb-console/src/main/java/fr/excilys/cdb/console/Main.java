package fr.excilys.cdb.console;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import fr.excilys.cdb.api.dto.Company;
import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.api.dto.Navigation;
import fr.excilys.cdb.api.dto.PageDto;

public class Main {
	private static Scanner sc = new Scanner(System.in);
	private static final String NO_COMPUTERS = "No computer Founded";
	private static final String NO_COMPANIES = "No computer Founded";
	private static final String ASK_ID_COMPUTER = "Please Entrer id of of Computer : ";
	private static final String ASK_NAME_COMPUTER = "Please Entrer Name of Computer : ";
	private static final String ASK_DATE_INTRODUCED = "Please Entrer date introduced of Computer (format yyyy-mm-dd or dd/mm/yyyy) :";
	private static final String ASK_DATE_DISCONTINUED = "Please Entrer date discontinued of Computer (format yyyy-mm-dd or dd/mm/yyyy) :";
	private static final String ASK_COMPANY_ID = "Please Entrer id of company of Computer if not please entre 0:";
	private static final String ID_NOT_BE_NULL = "Id Not be null or Negative";

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		int choice = 0;

		RestClient rest = new RestClient();
		
		boolean isContinue = true;
		LABEL :	while (isContinue) {
			printMenu();
			choice = insertChoice();
			switch (choice) {
				case 1 : printListComputers(rest); break;
				case 2 : printListCompanies(rest); break;
				case 3 : printDetailsComputer(rest); break;
				case 4 : createComputer(rest); break;
				case 5 : updateComputer(rest); break;
				case 6 : deleteComputer(rest); break;
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

	public static void printComputers(RestClient rest) {
		List<Computer> computers = rest.getAllComputer();
		printList(computers, NO_COMPUTERS);
	}

	public static <E> void printList(List<E> list, String message) {
		if (list.isEmpty()) {
			System.out.println(message);
			return;
		}
		list.forEach(System.out::println);
	}

	public static void printComputerswithPage(RestClient rest) {
		Navigation page = askPage();
		PageDto<Computer> computers = rest.getComputerWhithPage(page);
		printList(computers.getContent(), NO_COMPUTERS);
	}

	public static Navigation askPage() {
		System.out.print("Please Entrer your number page (!! first page begin at 1 !!): ");
		int numberPage = sc.nextInt();
		System.out.print("Please Entrer your size page : ");
		int sizePage = sc.nextInt();
		return getNavigation(numberPage, sizePage);
	}

	public static boolean isListWithPage() {
		System.out.println("Would you prefer get List with Page (0 => yes or 1 => no)");
		int choice = sc.nextInt();
		return choice == 0 ? true : false;
	}

	public static void printListComputers(RestClient rest) {
		if (isListWithPage()) {
			printComputerswithPage(rest);
		} else {
			printComputers(rest);
		}
	}

	public static void printListCompanies(RestClient rest) {
		if (isListWithPage()) {
			printCompaniesWithPage(rest);
		} else {
			printCompanies(rest);
		}
	}

	public static void printCompanies(RestClient rest) {
		List<Company> companies = rest.getAllCompanies();
		printList(companies, NO_COMPANIES);
	}
	public static void printCompaniesWithPage(RestClient rest) {
		Navigation page = askPage();
			PageDto<Company> companies = rest.getCompaniesWhithPage(page);
			printList(companies.getContent(), NO_COMPUTERS);
	}

	public static void printDetailsComputer(RestClient rest) {
		System.out.print("Entrer l'ID du computer : ");
		long id = sc.nextLong();
		if (id < 1) {
			System.out.print(ID_NOT_BE_NULL);
			return;
		}
		Optional<Computer> computer = rest.getComputerById(id);
		if(computer.isPresent()) {
			System.out.println("\n" + computer.get());
		} else {
			System.out.println("\n Computter with id : " + id + " Not exist");
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

	public static void createComputer(RestClient rest) {
		System.out.println("......................ADD COMPUTER..........................");
		Optional<Computer> computer = askComputer();
		if (!computer.isPresent()) {
			return;
		}
				int addCmputer = rest.createComputer(computer.get()).getStatus();
				switch (addCmputer) {
					case 400 : System.out.println("Computer is not added"); break;
					case 201 : System.out.println("Computer is added"); break;
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

		Computer computer = Computer.Builder.newInstance()
										  .setId(id)
										  .setName(name)
										  .setIntroduced(dateIntroduced)
										  .setDicontinued(dateDiscontinued)
										  .setIdCompany(companyId)
										  .build();
		return Optional.ofNullable(computer);
	}

	public static void deleteComputer(RestClient rest) {
		System.out.println("......................DELETE COMPUTER..........................");
		System.out.print(ASK_ID_COMPUTER);
		long id = sc.nextLong();
		if (id <= 0) {
			System.out.println(ID_NOT_BE_NULL);
			return;
		}
		 int delete = rest.deleteComputer(id).getStatus();
		switch (delete) {
			case 200 : System.out.println("Computer is deleted"); break;
			case 404 : System.out.println("Computer is not Found"); break;
		}
	}


	public static void updateComputer(RestClient rest) {
		System.out.println("......................UPDATE COMPUTER..........................");
		Optional<Computer> computer = askComputer();
		if (!computer.isPresent()) {
			return;
		}
			int update = rest.updateComputer(computer.get().getId(), computer.get()).getStatus();
			switch (update) {
				case 400 : System.out.println("Computer not updated"); break;
				case 201 : System.out.println("Computer is updated"); break;
			}
	}

	private static Navigation getNavigation(int number, int size) {
		Navigation navigation = Navigation.Builder.newInstance()
									.setNumber(number)
									.setSize(size)
									.build();
		return navigation;
	}
	
}
