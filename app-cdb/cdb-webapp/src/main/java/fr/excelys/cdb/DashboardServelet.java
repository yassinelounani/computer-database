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
import fr.excilys.cdb.api.dto.Page;
import fr.excilys.cdb.business.ComputerServiceExporter;


@WebServlet(name = "dashboard", urlPatterns = {"/dashboard"})
public class DashboardServelet extends HttpServlet {
	private static final int MARGIN_PAGINATE = 4;

	private static final long serialVersionUID = 1L;
	
	private static final int FIRST_PAGINATE = 1;
	private static final int END_PAGINATE = 8;
	private static final int SIZE_PAGE = 10;
	
	private ComputerService computerService;

	public DashboardServelet() {
		super();
		computerService = ComputerServiceExporter.getInstance();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pageRequest = request.getParameter("page");
		String sizeRequest = request.getParameter("size");
		int currentPage = (pageRequest != null) ? Integer.parseInt(pageRequest) : FIRST_PAGINATE;
		int  currentsize = (sizeRequest != null) ? Integer.parseInt(sizeRequest) : SIZE_PAGE;
		Page page = new Page(currentPage, currentsize);
		List<Computer> list = computerService.getComputersWithPage(page);
		int numberOfPages = computerService.getTotalOfPages(page);
		int endPage = (currentPage <= MARGIN_PAGINATE) ? END_PAGINATE : 
			(currentPage + MARGIN_PAGINATE > numberOfPages) ? numberOfPages : currentPage + MARGIN_PAGINATE;
		int startPage = currentPage <= MARGIN_PAGINATE ? FIRST_PAGINATE : currentPage - MARGIN_PAGINATE;

		request.setAttribute("computers", list);
		request.setAttribute("totalOfPages", numberOfPages);
		request.setAttribute("endPage", endPage);
		request.setAttribute("startPage", startPage);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("size", currentsize);
		
        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
