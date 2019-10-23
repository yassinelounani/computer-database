package fr.excelys.cdb;

import static fr.excelys.cdb.HelperFront.buildComputer;
import static fr.excelys.cdb.HelperFront.checkError;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.excilys.cdb.api.CompanyService;
import fr.excilys.cdb.api.ComputerService;
import fr.excilys.cdb.api.dto.Company;
import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.api.dto.ComputerId;
import fr.excilys.cdb.api.exception.NotFoundComputerException;
import fr.excilys.cdb.business.CompanyServiceExporter;
import fr.excilys.cdb.business.ComputerServiceExporter;

@WebServlet(name = "editComputer", urlPatterns = {"/editComputer"})
public class EditComputer extends HttpServlet {

	private static final long serialVersionUID = 3L;

	private ComputerService computerService;
	private CompanyService companyService;
	
	public EditComputer() {
        super();
        computerService = ComputerServiceExporter.getInstance();
        companyService = CompanyServiceExporter.getInstance();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		long idComputer = Long.parseLong(id);
		ComputerId computerId = new ComputerId(idComputer);
		System.err.println(computerId);
		Computer computer = computerService.getComputerById(computerId).get();
		System.err.println(computer);
		List<Company> companies = companyService.getCompanies();
		
		request.setAttribute("computer", computer);
		request.setAttribute("companies", companies);
		
		request.getRequestDispatcher("/views/EditComputer.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Computer computer = buildComputer(request);
		Map<String, String> messages = checkError(computer);
		updateComputer(request, computer, messages);
		
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
