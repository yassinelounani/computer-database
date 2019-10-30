package fr.excelys.cdb;

import static fr.excelys.cdb.HelperFront.buildComputer;
import static fr.excelys.cdb.HelperFront.checkError;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fr.excilys.cdb.api.CompanyService;
import fr.excilys.cdb.api.ComputerService;
import fr.excilys.cdb.api.dto.Company;
import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.api.dto.Identifier;
import fr.excilys.cdb.api.exception.NotFoundComputerException;
import fr.excilys.cdb.business.CompanyServiceExporter;
import fr.excilys.cdb.business.ComputerServiceExporter;

@Controller
@WebServlet(name = "editComputer", urlPatterns = {"/editComputer"})
public class EditComputer extends HttpServlet {

	private static final long serialVersionUID = 3L;
	@Autowired
	private ComputerService computerService;
	@Autowired
	private CompanyService companyService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		long idComputer = Long.parseLong(id);
		Identifier computerId = new Identifier(idComputer);
		Computer computer = computerService.getComputerById(computerId).get();
		List<Company> companies = companyService.getCompanies();
		
		request.setAttribute("computer", computer);
		request.setAttribute("companies", companies);
		
		request.getRequestDispatcher("/views/EditComputer.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		long idComputer = Long.parseLong(id);
		Computer computer = buildComputer(request);
		computer.setId(idComputer);
		Map<String, String> messages = checkError(computer);
		System.err.println(messages);
		updateComputer(request, computer, messages);
		request.setAttribute("messages", messages);
		request.getRequestDispatcher("/views/EditComputer.jsp").forward(request, response);
	}
	
	private void updateComputer(HttpServletRequest request, Computer computer, Map<String, String> messages) {
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
		} else {
			request.setAttribute("computer", computer);
		}
	}

}
