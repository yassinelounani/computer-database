package fr.excelys.cdb;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.excilys.cdb.api.ComputerService;
import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.business.ComputerServiceExporter;


@WebServlet("/dashboard")
public class Servelet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Servelet() {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ComputerService computerService = ComputerServiceExporter.getInstance();
		List<Computer> list = computerService.getComputers();
		for (Computer c : list) {
			System.out.println(c);
		}
		
		request.setAttribute("computers", list);
		

		request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	

	

}
