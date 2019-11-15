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
import org.springframework.web.bind.annotation.RequestParam;
import fr.excilys.cdb.api.CompanyService;
import fr.excilys.cdb.api.ComputerService;
import fr.excilys.cdb.api.dto.Company;
import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.api.dto.Identifier;
import fr.excilys.cdb.api.dto.NameAndPage;
import fr.excilys.cdb.api.dto.Navigation;
import fr.excilys.cdb.api.dto.Page;
import fr.excilys.cdb.api.dto.PageAndSort;
import fr.excilys.cdb.api.dto.Pagination;
import fr.excilys.cdb.api.dto.Pagination.Builder;
import fr.excilys.cdb.api.dto.Sort;
import fr.excilys.cdb.api.exception.NotFoundCompanyException;
import fr.excilys.cdb.api.exception.NotFoundComputerException;

@Controller
public class Dashboard {
	private static final String REDIRECT_DASHBOARD = "redirect:/dashboard";
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
		List<Computer> computers = null;
		int numberOfPages = 0;
		Page page = getPage(navigation.getNumber(), navigation.getSize());
		Sort sort = new Sort(navigation.getProperty(), navigation.getOrder());
		if(!isBlank(navigation.getName())) {
			NameAndPage nameAndPage = getNameAndPage(navigation.getName(), page);
			computers = computerService.getSerchComputersWithPage(nameAndPage);
			numberOfPages = computerService.getTotalPagesOfSerchedComputers(nameAndPage);
			model.addAttribute("name", navigation.getName());
		} else if (!isBlank(navigation.getProperty()) && !isBlank(navigation.getOrder())) {
			PageAndSort pageAndSort = new PageAndSort(page, sort);
			computers = computerService.getComputersWithPageAndSort(pageAndSort);
			numberOfPages = computerService.getTotalPagesOfComputers(page);
			model.addAttribute("sort", sort);
		} else {
			computers = computerService.getComputersWithPage(page);
			numberOfPages = computerService.getTotalPagesOfComputers(page);
		}
		Pagination navPagination = getPagination(page, numberOfPages);
		model.addAttribute("computers", computers);
		model.addAttribute("pagination", navPagination);
		return DASHBOARD;
	}

	@PostMapping("dashboard/delete")
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

	@PostMapping("/addComputer")
	public String addComputer(@ModelAttribute("computer") Computer computer, Model model) {
		Map<String, String> messages = 	checkError(computer);
		addComputer(model, computer, messages);
		SendComputer(model, messages);
		return ADD_COMPUTER;
	}

	@GetMapping("/editComputer")
	public String updateComputer(@RequestParam("id") int idComputer, Model model) {
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
		updateComputer(model, computer, messages);
		return EDIT_COMPUTER;
		
	}
	private List<Identifier> getSelectedIds(List<String> ids) {
		return ids.stream().map(id -> new Identifier(Long.parseLong(id))).collect(Collectors.toList());
	}

	private Page getPage( int number, int size) {
		return number == 0 ? new Page(FIRST_PAGINATE, SIZE_PAGE) : new Page(number, size);
	}

	private boolean isBlank(String request) {
		return request == null || request.isEmpty();
	}

	private NameAndPage getNameAndPage(String name, Page page) {
		NameAndPage nameAndPage = NameAndPage.Builder.newInstance()
				.setName(name)
				.setPage(page)
				.build();
		return nameAndPage;
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
				int addValue = computerService.addComputer(computer);
				if ( addValue == 0) {
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

	private void SendComputer(Model model, Map<String, String> messages) {
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

	private void updateComputer(Model model, Computer computer, Map<String, String> messages) {
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
		model.addAttribute("computer", computer);
		model.addAttribute("messages", messages);
	}

	private Pagination getPagination(Page page, int numberOfPages) {
		int currentPage = page.getNumber();
		int currentSize = page.getSize();
		int startPage = getBeginPage(page.getNumber());
		int endPage = getEndPage(numberOfPages, currentPage);
		return Builder.newInstance()
					.setNumbersOfPages(numberOfPages)
					.setStartPage(startPage)
					.setEndPage(endPage)
					.setCurrentPage(currentPage)
					.setSize(currentSize)
					.build();
	}


}
