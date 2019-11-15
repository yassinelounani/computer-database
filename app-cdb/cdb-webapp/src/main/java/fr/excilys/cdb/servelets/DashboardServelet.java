package fr.excilys.cdb.servelets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fr.excilys.cdb.api.ComputerService;
import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.api.dto.Identifier;
import fr.excilys.cdb.api.dto.NameAndPage;
import fr.excilys.cdb.api.dto.NameAndPage.Builder;
import fr.excilys.cdb.api.dto.Page;
import fr.excilys.cdb.api.dto.PageAndSort;
import fr.excilys.cdb.api.dto.Sort;
import fr.excilys.cdb.api.exception.NotFoundComputerException;

public class DashboardServelet extends HttpServlet {
	private static final int MARGIN_PAGINATE = 4;

	private static final long serialVersionUID = 1L;
	
	private static final int FIRST_PAGINATE = 1;
	private static final int END_PAGINATE = 8;
	private static final int SIZE_PAGE = 10;

	@Autowired
	private ComputerService computerService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int numberOfPages = 0;
		List<Computer> computers = null;
		String pageRequest = request.getParameter("page");
		String nameRequest = request.getParameter("search");
		String sizeRequest = request.getParameter("size");
		String sortRequest = request.getParameter("sort");
		String sortByRequest = request.getParameter("by");
		int currentPage = (pageRequest != null) ? Integer.parseInt(pageRequest) : FIRST_PAGINATE;
		int  currentsize = (sizeRequest != null) ? Integer.parseInt(sizeRequest) : SIZE_PAGE;
		Page page = new Page(currentPage, currentsize);
		if (isNotBlank(nameRequest)) {
			NameAndPage nameAndPage = Builder.newInstance()
					.setName(nameRequest)
					.setPage(page)
					.build();
			computers = computerService.getSerchComputersWithPage(nameAndPage);
			numberOfPages = computerService.getTotalPagesOfSerchedComputers(nameAndPage);
		} else if (isNotBlank(sortRequest) && isNotBlank(sortByRequest)){
			Sort sort = new Sort(sortRequest, sortByRequest);
			PageAndSort pageAndSort = new PageAndSort(page, sort);
			computers = computerService.getComputersWithPageAndSort(pageAndSort);
			numberOfPages = computerService.getTotalPagesOfComputers(page);
		} else {
			computers = computerService.getComputersWithPage(page);
			numberOfPages = computerService.getTotalPagesOfComputers(page);
		}
		
		int startPage = getBeginPage(currentPage);
		int endPage = getEndPage(numberOfPages, currentPage);
		
		request.setAttribute("computers", computers);
		request.setAttribute("totalOfPages", numberOfPages);
		request.setAttribute("endPage", endPage);
		request.setAttribute("startPage", startPage);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("size", currentsize);
        request.setAttribute("search", nameRequest);
        request.setAttribute("by", sortByRequest);
        request.setAttribute("sort", sortRequest);
       
        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
	}

	private boolean isNotBlank(String request) {
		return request != null && request.isEmpty();
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			List<Identifier> computerIds = getSelectedIds(request);
			for(Identifier computerid : computerIds) {
				try {
					computerService.deleteComputerById(computerid);
				} catch (NotFoundComputerException e) {
					e.printStackTrace();
				}
			}
		doGet(request,response);
	}

	private List<Identifier> getSelectedIds(HttpServletRequest request) {
		String[] ids = request.getParameterValues("selection");
		String[] splitIds = ids[0].split(",");
		List<Identifier> computersIds = new ArrayList<>();
		for(String id : splitIds) {
	    	computersIds.add(new Identifier(Long.parseLong(id)));
	    }
		return computersIds;
	}


}
