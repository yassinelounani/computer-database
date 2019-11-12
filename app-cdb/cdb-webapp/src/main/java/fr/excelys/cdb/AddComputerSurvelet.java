package fr.excelys.cdb;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fr.excilys.cdb.api.CompanyService;
import fr.excilys.cdb.api.ComputerService;
import fr.excilys.cdb.api.dto.Company;
import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.api.dto.Computer.Builder;
import fr.excilys.cdb.api.exception.NotFoundCompanyException;

@Controller
@WebServlet(name = "addComputer", urlPatterns = {"/addComputer"})
public class AddComputerSurvelet extends HttpServlet {
	private static final long serialVersionUID = 2L;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ComputerService computerService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Company> companies = companyService.getCompanies();
		request.setAttribute("companies", companies);
		request.getRequestDispatcher("/views/AddComputer.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Computer computer = buildComputer(request);
		Map<String, String> messages = 	checkError(computer);
		addComputer(request, computer, messages);
		SendComputer(request, messages);
		request.getRequestDispatcher("/views/AddComputer.jsp").forward(request, response);
	}

	private void addComputer(HttpServletRequest request, Computer computer, Map<String, String> messages) {
		if (messages.size() == 0) {
			try {
				int addValue = computerService.addComputer(computer);
				if ( addValue == 0) {
					messages.put("fail", "some thing rong");
				} else {
					messages.put("success", "Computer added with success");
				}
			} catch (NotFoundCompanyException e) {
				messages.put("company", e.getMessage());
			}
		} else {
			request.setAttribute("computer", computer);
		}
	}

	private void SendComputer(HttpServletRequest request, Map<String, String> messages) {
		List<Company> companies = companyService.getCompanies();
		request.setAttribute("companies", companies);
		request.setAttribute("messages", messages);
	}

	private Computer buildComputer(HttpServletRequest request) {
		String computerName = (String) request.getParameter("name");
		String introduced = (String) request.getParameter("introduced");
		String discontinued = (String) request.getParameter("discontinued");
		String companyId = (String) request.getParameter("companyId");
		Computer computer = Builder.newInstance()
								.setName(computerName)
								.setIntroduced(introduced)
								.setDicontinued(discontinued)
								.setIdCompany(Long.parseLong(companyId))
								.build();
		return computer;
	}

	public Map<String, String> checkError(Computer computer) {
		Map<String, String> messages = new HashMap<String, String>();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	    Validator validator = factory.getValidator(); 
		Set<ConstraintViolation<Computer>> violations = validator.validate(computer);
		if (violations.size() > 0 ) {
		      for (ConstraintViolation<Computer> contraintes : violations) {
		        messages.put(
			        		contraintes.getPropertyPath().toString(),
			        		contraintes.getPropertyPath().toString() + " " + contraintes.getMessage()
		        		);	 
		      }
		}
		return messages;
	}

}
