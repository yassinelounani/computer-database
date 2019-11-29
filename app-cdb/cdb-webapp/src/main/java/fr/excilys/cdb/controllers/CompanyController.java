package fr.excilys.cdb.controllers;

import static org.springframework.http.ResponseEntity.ok;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.excilys.cdb.api.CompanyService;
import fr.excilys.cdb.api.dto.Company;
import fr.excilys.cdb.api.dto.Navigation;
import fr.excilys.cdb.api.dto.PageDto;
import io.swagger.annotations.ApiOperation;
@RestController
@RequestMapping("/companies")
public class CompanyController {

	private final CompanyService companyService;

	@Autowired
	public CompanyController(CompanyService companyService) {
		super();
		this.companyService = companyService;
	}
	
	@GetMapping
	@ApiOperation(value = "${swagger.companies}", notes = "${swagger.companies.desc}")
	public ResponseEntity<List<Company>> getAll() {
		List<Company> companies = companyService.getCompanies();
		return ok().body(companies); 
	}

	@GetMapping("/page")
	@ApiOperation(value = "${swagger.comp.page}", notes = "${swagger.comp.page.desc}")
	public ResponseEntity<PageDto<Company>> getAllWithPage(Navigation navigation) {
		PageDto<Company> pageDto = listComputers(navigation);
		return ok().body(pageDto);
	}

	private PageDto<Company> listComputers(Navigation navigation) {
		PageDto<Company> page = getPage(navigation);
		return companyService.getCompaniesWithPage(page);
	}

	private PageDto<Company> getPage(Navigation navigation) {
		return getRequestPage(navigation);
	}

	private PageDto<Company> getRequestPage(Navigation navigation) {
		PageDto.Builder<Company> builder = new PageDto.Builder<>();
		return builder.setNumber(navigation.getNumber())
			.setSize(navigation.getSize())
			.build();
	}
}
