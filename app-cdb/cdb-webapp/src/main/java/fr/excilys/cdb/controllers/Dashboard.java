package fr.excilys.cdb.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import fr.excilys.cdb.api.CompanyService;
import fr.excilys.cdb.api.ComputerService;
import fr.excilys.cdb.api.dto.Company;
import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.api.dto.Identifier;
import fr.excilys.cdb.api.dto.Navigation;
import fr.excilys.cdb.api.dto.PageDto;
import fr.excilys.cdb.api.dto.Pagination;
import fr.excilys.cdb.api.dto.Pagination.Builder;
import fr.excilys.cdb.api.dto.SortDto;
import fr.excilys.cdb.api.exception.NotFoundCompanyException;
import fr.excilys.cdb.api.exception.NotFoundComputerException;


@Controller
@RequestMapping("/computers")
public class Dashboard {
	private static final String REDIRECT_DASHBOARD = "redirect:/computers/dashboard";
	private static final String EDIT_COMPUTER = "EditComputer";
	private static final String ADD_COMPUTER = "AddComputer";
	private static final String DASHBOARD = "dashboard";
	private static final int MARGIN_PAGINATE = 4;
	private static final int FIRST_PAGINATE = 1;
	private static final int END_PAGINATE = 8;
	private static final int SIZE_PAGE = 10;

	private final ComputerService computerService;
	private final CompanyService companyService;

	@Autowired
	public Dashboard(ComputerService computerService, CompanyService companyService) {
		super();
		this.computerService = computerService;
		this.companyService = companyService;
	}

	@GetMapping("/dashboard")
	public String dashboard(@ModelAttribute("navigation") Navigation navigation, Model model) {
		Pagination pagination = null;
		PageDto<Computer> page = getPage(navigation);
		SortDto sort = new SortDto(navigation.getProperty(), navigation.getOrder());
		if(!isBlank(navigation.getName())) {
			pagination = findComputers(page, navigation.getName());
			model.addAttribute("name", navigation.getName());
		} else if (!isBalnkSort(navigation)) {
			pagination = sortComputers(page, sort);
			model.addAttribute("sort", sort);
		} else {
			pagination = listComputers(page);
		}
		model.addAttribute("pagination", pagination);
		return DASHBOARD;
	}

	@PostMapping("/deleteComputers")
	public String deleteComputers(@RequestParam(value="selection") List<String> ids) {
		List<Identifier> computerIds = getSelectedIds(ids);
		for(Identifier computerid : computerIds) {
			try {
				computerService.deleteComputerById(computerid);
			} catch (NotFoundComputerException e) {
				System.out.println(e.getMessage());
			}
		}
		return REDIRECT_DASHBOARD;
	}

	@GetMapping("/addComputer")
	public String getCompanies(Model model) {
		List<Company> companies = companyService.getCompanies();
		model.addAttribute("companies", companies);
		return ADD_COMPUTER;
	}

	@GetMapping("/login")
	public String login(Model model) {
		
		return "login";
	}

	@PostMapping("/addComputer")
	public String addComputer(@ModelAttribute("computer") Computer computer, Model model) {
		Map<String, String> messages = 	checkError(computer);
		addComputer(model, computer, messages);
		SendMessages(model, messages);
		return ADD_COMPUTER;
	}

	@GetMapping("/editComputer")
	public String updateComputer(@RequestParam("id") long idComputer, Model model) {
		Identifier computerId = new Identifier(idComputer);
		Computer computer = computerService.getComputerById(computerId).get();
		List<Company> companies = companyService.getCompanies();
		model.addAttribute("computer", computer);
		model.addAttribute("companies", companies);
		return EDIT_COMPUTER;
	}

	@PostMapping("/editComputer")
	public String updateComputer(@ModelAttribute("computer") Computer computer, Model model) {
		Map<String, String> messages = checkError(computer);
		updateAndSaveMessages(model, computer, messages);
		return updateComputer(computer.getId(), model);
		
	}
	private List<Identifier> getSelectedIds(List<String> ids) {
		return ids.stream().map(id -> new Identifier(Long.parseLong(id))).collect(Collectors.toList());
	}

	private PageDto<Computer> getPage(Navigation navigation) {
		return navigation.getNumber() == 0 ? getFirstPage() : getRequestPage(navigation);
	}

	private PageDto<Computer> getFirstPage() {
		PageDto.Builder<Computer> builder = new PageDto.Builder<>();
		return builder.setNumber(FIRST_PAGINATE)
			.setSize(SIZE_PAGE)
			.build();
	}

	private PageDto<Computer> getRequestPage(Navigation navigation) {
		PageDto.Builder<Computer> builder = new PageDto.Builder<>();
		return builder.setNumber(navigation.getNumber())
			.setSize(navigation.getSize())
			.build();
		
	}
	
	private boolean isBlank(String request) {
		return request == null || request.isEmpty();
	}

	private int getBeginPage(int currentPage) {
		return currentPage <= MARGIN_PAGINATE ? FIRST_PAGINATE : currentPage - MARGIN_PAGINATE;
	}

	private int getEndPage(int numberOfPages, int currentPage) {
		int endPage;
		if (numberOfPages <= END_PAGINATE) {
			endPage =  numberOfPages; 
		} else if (currentPage <= END_PAGINATE) {
			endPage = END_PAGINATE;
		} else {
			endPage = (currentPage + MARGIN_PAGINATE > numberOfPages) ? numberOfPages : currentPage + MARGIN_PAGINATE;
		}
		return endPage;
	}

	private void addComputer(Model model, Computer computer, Map<String, String> messages) {
		if (messages.size() == 0) {
			try {
				int addComputer = computerService.addComputer(computer);
				if (addComputer == 0) {
					messages.put("fail", "some thing rong");
				} else {
					messages.put("success", "Computer added with success");
					computer = null;
				}
			} catch (NotFoundCompanyException e) {
				messages.put("company", e.getMessage());
			}
		} 
		model.addAttribute("computer", computer);
	}

	private void SendMessages(Model model, Map<String, String> messages) {
		List<Company> companies = companyService.getCompanies();
		model.addAttribute("companies", companies);
		model.addAttribute("messages", messages);
	}

	private Map<String, String> checkError(Computer computer) {
		Map<String, String> messages = new HashMap<String, String>();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	    Validator validator = factory.getValidator(); 
		Set<ConstraintViolation<Computer>> violations = validator.validate(computer);
		if (violations.size() > 0 ) {
		      for (ConstraintViolation<Computer> contraintes : violations) {
		        messages.put(contraintes.getPropertyPath().toString(),
			        		 contraintes.getPropertyPath().toString() + " " + contraintes.getMessage());	 
		      }
		}
		return messages;
	}

	private void updateAndSaveMessages(Model model, Computer computer, Map<String, String> messages) {
		if (messages.size() == 0) {
			try {
				int updateValue = computerService.updateComputer(computer);
				if ( updateValue == 0) {
					messages.put("fail", "some thing rong");
				} else {
					messages.put("success", "Computer updated with success");
				}
			} catch (NotFoundComputerException e) {
				messages.put("company", e.getMessage());
			}
		}
		model.addAttribute("messages", messages);
	}

	private Pagination getPagination(PageDto<Computer> page) {
		int currentPage = page.getNumber();
		int currentSize = page.getSize();
		int startPage = getBeginPage(page.getNumber());
		int numberOfPages = getTotalPages(page);
		int endPage = getEndPage(numberOfPages, currentPage);
		return Builder.newInstance()
					.setNumbersOfPages(numberOfPages)
					.setStartPage(startPage)
					.setEndPage(endPage)
					.setCurrentPage(currentPage)
					.setSize(currentSize)
					.setComputers(page.getContent())
					.build();
	}

	private Pagination findComputers(PageDto<Computer> page, String name) {
		PageDto<Computer> computers = computerService.getSerchComputersWithPage(page, name);
		return getPagination(computers);
	}

	private Pagination sortComputers(PageDto<Computer> page, SortDto sort) {
		PageDto<Computer> computers = computerService.getComputersWithPageAndSort(page, sort);
		return getPagination(computers);
	}

	private Pagination listComputers(PageDto<Computer> page) {
		PageDto<Computer> computers = computerService.getComputersWithPage(page);
		return getPagination(computers);
	}

	private int getTotalPages(PageDto<Computer> page) {
		if(page.getTotalElement() <9) {
			return FIRST_PAGINATE;
		}
		int totalPages = (int) (page.getTotalElement() / page.getSize());
		totalPages = totalPages % page.getSize() == 0 ? totalPages : totalPages + 1;
		return totalPages;
	}

	private boolean isBalnkSort(Navigation navigation) {
		return isBlank(navigation.getProperty())  || isBlank(navigation.getOrder());
	}
} 
